package com.lloyds.offers.auth.api.dto;

import jakarta.validation.constraints.*;
import java.util.UUID;

public record RegisterRequest(
    @NotBlank String identifier,
    @NotBlank @Pattern(regexp = "phone|email") String identifierType,
    @NotBlank String deviceId
) {}

public record VerifyOtpRequest(
    @NotNull UUID userId,
    @NotBlank @Pattern(regexp = "\\d{6}") String code,
    String deviceId
) {}

public record ResendOtpRequest(@NotNull UUID userId) {}

public record LoginRequest(
    @NotBlank String identifier,
    @NotBlank String password,
    @NotBlank String deviceId
) {}

public record RefreshRequest(@NotBlank String refreshToken) {}

public record LogoutRequest(@NotBlank String deviceId) {}

public record ConsentRequest(
    @NotBlank @Pattern(regexp = "location|transaction") String consentType,
    @NotBlank String decision,
    @NotBlank String version
) {}

public record SuccessResponse(boolean success) {}
