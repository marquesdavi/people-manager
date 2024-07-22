package com.api.manager.people.service;

import com.api.manager.people.model.dto.auth.LoginRequest;
import com.api.manager.people.model.dto.auth.TokenResponse;

public interface AuthenticationService {
    TokenResponse login(LoginRequest request);
}
