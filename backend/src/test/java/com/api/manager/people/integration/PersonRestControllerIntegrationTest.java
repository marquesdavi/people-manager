package com.api.manager.people.integration;

import com.api.manager.people.PeopleApplication;
import com.api.manager.people.domain.permission.Role;
import com.api.manager.people.repository.IRoleRepository;
import com.api.manager.people.repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = PeopleApplication.class)
@AutoConfigureMockMvc
@TestPropertySource(
        locations = "classpath:application-test.properties")
@EnableConfigurationProperties
@RequiredArgsConstructor
public class PersonRestControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private IRoleRepository roleRepository;

    @Autowired
    private IUserRepository userRepository;

    private static final String USER_EMAIL = "zedavenda@teste.com";
    private static final String USER_PASSWORD = "10203040";
    private static final String USER_FIRST_NAME = "Zé";
    private static final String USER_LAST_NAME = "Da Venda";

    private String accessToken;

    @BeforeEach
    void setUp() throws Exception {
        initializeRoles();
        userRepository.deleteAll();
        registerTestUser();
        authenticateTestUser();
    }

    private void initializeRoles() {
        if (!roleRepository.findByName("USER").isPresent()) {
            Role userRole = new Role();
            userRole.setName("USER");
            userRole.setDescription("Default user Role");
            roleRepository.save(userRole);
        }
    }

    private void registerTestUser() throws Exception {
        mockMvc.perform(post("/user/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.format("{\"email\":\"%s\",\"password\":\"%s\",\"firstName\":\"%s\",\"lastName\":\"%s\"}",
                                USER_EMAIL, USER_PASSWORD, USER_FIRST_NAME, USER_LAST_NAME)))
                .andExpect(status().isOk());
    }

    private void authenticateTestUser() throws Exception {
        String response = mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.format("{\"email\":\"%s\",\"password\":\"%s\"}", USER_EMAIL, USER_PASSWORD)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").exists())
                .andReturn()
                .getResponse()
                .getContentAsString();

        accessToken = response.substring(response.indexOf(":\"") + 2, response.indexOf("\",\"expiresIn\""));
    }

    @Test
    void whenGetPerson_thenStatus200() throws Exception {
        createPerson("John Doe", "john.doe@example.com", "77533010043", "1980-01-01");

        mockMvc.perform(get("/person/1")
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"))
                .andExpect(jsonPath("$.cpf").value("77533010043"))
                .andExpect(jsonPath("$.birthDate").value("1980-01-01"));
    }

    @Test
    void whenCreatePerson_thenStatus201() throws Exception {
        mockMvc.perform(post("/person/")
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Jane Doe\",\"email\":\"jane.doe@example.com\",\"cpf\":\"28949211017\",\"birthDate\":\"1990-02-01\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Person successfully created!"));
    }

    @Test
    void whenUpdatePerson_thenStatus200() throws Exception {
        createPerson("Jane Doe", "jane.doe@example.com", "15682503040", "1990-02-01");

        mockMvc.perform(patch("/person/1")
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Jane Smith\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Person successfully updated!"));
    }

    @Test
    void whenDeletePerson_thenStatus200() throws Exception {
        createPerson("John Doe", "john.doe@example.com", "19247061059", "1980-01-01");

        mockMvc.perform(delete("/person/1")
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Person successfully deleted!"));
    }

    private void createPerson(String name, String email, String cpf, String birthDate) throws Exception {
        mockMvc.perform(post("/person/")
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.format("{\"name\":\"%s\",\"email\":\"%s\",\"cpf\":\"%s\",\"birthDate\":\"%s\"}",
                                name, email, cpf, birthDate)))
                .andExpect(status().isCreated());
    }
}
