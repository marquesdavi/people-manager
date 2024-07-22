package com.api.manager.people.mapper;

import com.api.manager.people.domain.permission.Role;
import com.api.manager.people.domain.user.User;
import com.api.manager.people.model.dto.user.RegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class UserMapper {
    private final BCryptPasswordEncoder passwordEncoder;

    public User toEntity(RegisterRequest registerRequest, Set<Role> roles) {
        if (registerRequest == null) {
            return null;
        }

        User user = new User();
        BeanUtils.copyProperties(registerRequest, user);
        user.setPassword(passwordEncoder.encode(registerRequest.password()));
        user.setRole(roles);
        return user;
    }
}
