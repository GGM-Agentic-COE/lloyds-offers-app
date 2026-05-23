package com.lloyds.offers.auth.application.dto;

import java.time.Instant;
import java.util.UUID;

public record RegisterResponse(UUID userId, boolean verificationRequired, int expiresIn) {}

public record TokenResponse(String accessToken, String refreshToken, int expiresIn, String tokenType) {
    public TokenResponse(String accessToken, String refreshToken, int expiresIn) {
        this(accessToken, refreshToken, expiresIn, "Bearer");
    }
}

public record ConsentResponse(UUID consentId, Instant recordedAt) {}

public record ResendOtpResponse(boolean sent, int expiresIn) {}
