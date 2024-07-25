package com.api.manager.people.service.impl;

import com.api.manager.people.domain.user.User;
import com.api.manager.people.domain.permission.Role;
import com.api.manager.people.model.dto.auth.LoginRequest;
import com.api.manager.people.model.dto.auth.TokenResponse;
import com.api.manager.people.repository.IUserRepository;
import com.api.manager.people.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationServiceImplementation implements AuthenticationService {

    private final JwtEncoder jwtEncoder;
    private final JwtDecoder jwtDecoder;
    private final IUserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Value("${jwt.token.expires-in:3600}")
    private long expiresIn;

    @Override
    public TokenResponse login(LoginRequest request) {
        Optional<User> user = userRepository.findByEmail(request.email());

        if (user.isEmpty() || !user.get().isLoginCorrect(request, passwordEncoder)) {
            log.warn("Failed login attempt for email: {}", request.email());
            throw new BadCredentialsException("User or password is invalid!");
        }

        var now = Instant.now();

        var scopes = user.get().getRole()
                .stream()
                .map(Role::getName)
                .collect(Collectors.joining(" "));

        var claims = JwtClaimsSet.builder()
                .issuer("peopleauth")
                .subject(user.get().getId().toString())
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expiresIn))
                .claim("scope", scopes)
                .claim("roles", user.get().getRole().stream()
                        .map(Role::getName) 
                        .collect(Collectors.toList()))
                .build();

        var jwtValue = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

        return new TokenResponse(jwtValue, expiresIn);
    }

    @Override
    public boolean validateToken(String token) {
        try {
            if (token.startsWith("Bearer ")) {
                token = token.substring(7);
            }
            jwtDecoder.decode(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }
}
