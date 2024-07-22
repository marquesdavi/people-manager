package com.api.manager.people.model.dto.auth;

public record TokenResponse(String accessToken, Long expiresIn) {
}
