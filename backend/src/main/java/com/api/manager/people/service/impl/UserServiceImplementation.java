package com.api.manager.people.service.impl;

import com.api.manager.people.domain.permission.Role;
import com.api.manager.people.domain.user.User;
import com.api.manager.people.exception.AlreadyExistsException;
import com.api.manager.people.exception.NotFoundException;
import com.api.manager.people.mapper.UserMapper;
import com.api.manager.people.model.dto.user.RegisterRequest;
import com.api.manager.people.repository.IRoleRepository;
import com.api.manager.people.repository.IUserRepository;
import com.api.manager.people.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImplementation implements UserService {
    private final IUserRepository userRepository;
    private final IRoleRepository roleRepository;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public void create(RegisterRequest dto) {
        log.info("Creating new user with email: {}", dto.email());

        Role userRole = roleRepository.findByName(Role.Values.USER.name())
                .orElseThrow(() -> {
                    log.error("USER role not found!");
                    return new NotFoundException("USER role not found");
                });

        userRepository.findByEmail(dto.email()).ifPresent(existingUser -> {
            log.warn("User with email {} already exists!", dto.email());
            throw new AlreadyExistsException("User already exists");
        });

        User user = userMapper.toEntity(dto, Set.of(userRole));
        userRepository.save(user);

        log.info("User with email {} successfully created.", dto.email());
    }

    @Override
    public List<User> list() {
        return userRepository.findAll();
    }
}
