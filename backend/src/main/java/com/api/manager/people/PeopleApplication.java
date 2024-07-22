package com.api.manager.people;

import com.api.manager.people.domain.permission.Role;
import com.api.manager.people.repository.IRoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@Slf4j
@EnableCaching
@SpringBootApplication
@RequiredArgsConstructor
public class PeopleApplication implements CommandLineRunner {
	private final IRoleRepository roleRepository;

	public static void main(String[] args) {
		SpringApplication.run(PeopleApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		createRoleIfNotFound("ADMIN", "Administrator role");
		createRoleIfNotFound("USER", "Default user role");
	}

	private void createRoleIfNotFound(String roleName, String description) {
		if (!roleRepository.findByName(roleName).isPresent()) {
			log.info("Creating role {}", roleName);

			Role role = new Role();
			role.setName(roleName);
			role.setDescription(description);
			roleRepository.save(role);

			log.info("Role {} created successfully", roleName);
		}
	}
}
