package com.lloyds.offers.auth.api.controller;

import com.lloyds.offers.auth.api.dto.*;
import com.lloyds.offers.auth.application.command.*;
import com.lloyds.offers.auth.application.command.RegisterUserHandler.RegisterCommand;
import com.lloyds.offers.auth.application.command.VerifyOtpHandler.VerifyOtpCommand;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final RegisterUserHandler registerHandler;
    private final VerifyOtpHandler verifyOtpHandler;
    private final ResendOtpHandler resendOtpHandler;
    private final LoginHandler loginHandler;
    private final RefreshTokenHandler refreshHandler;
    private final LogoutHandler logoutHandler;
    private final RecordConsentHandler consentHandler;
    private final GetConsentHandler getConsentHandler;

    public AuthController(RegisterUserHandler registerHandler, VerifyOtpHandler verifyOtpHandler,
                          ResendOtpHandler resendOtpHandler, LoginHandler loginHandler,
                          RefreshTokenHandler refreshHandler, LogoutHandler logoutHandler,
                          RecordConsentHandler consentHandler, GetConsentHandler getConsentHandler) {
        this.registerHandler = registerHandler;
        this.verifyOtpHandler = verifyOtpHandler;
        this.resendOtpHandler = resendOtpHandler;
        this.loginHandler = loginHandler;
        this.refreshHandler = refreshHandler;
        this.logoutHandler = logoutHandler;
        this.consentHandler = consentHandler;
        this.getConsentHandler = getConsentHandler;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        var result = registerHandler.handle(new RegisterCommand(
                request.identifier(), request.identifierType(), request.deviceId()));
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.of(result));
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@Valid @RequestBody VerifyOtpRequest request) {
        var result = verifyOtpHandler.handle(new VerifyOtpCommand(
                request.userId(), request.code(), request.deviceId()));
        return ResponseEntity.ok(ApiResponse.of(result));
    }

    @PostMapping("/resend-otp")
    public ResponseEntity<?> resendOtp(@Valid @RequestBody ResendOtpRequest request) {
        var result = resendOtpHandler.handle(request.userId());
        return ResponseEntity.ok(ApiResponse.of(result));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        var result = loginHandler.handle(request.identifier(), request.password(), request.deviceId());
        return ResponseEntity.ok(ApiResponse.of(result));
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@Valid @RequestBody RefreshRequest request) {
        var result = refreshHandler.handle(request.refreshToken());
        return ResponseEntity.ok(ApiResponse.of(result));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@Valid @RequestBody LogoutRequest request) {
        logoutHandler.handle(getCurrentUserId(), request.deviceId());
        return ResponseEntity.ok(ApiResponse.of(new SuccessResponse(true)));
    }

    @PostMapping("/consent")
    public ResponseEntity<?> recordConsent(@Valid @RequestBody ConsentRequest request) {
        var result = consentHandler.handle(getCurrentUserId(), request.consentType(), request.decision(), request.version());
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.of(result));
    }

    @GetMapping("/consent")
    public ResponseEntity<?> getConsent() {
        var result = getConsentHandler.handle(getCurrentUserId());
        return ResponseEntity.ok(ApiResponse.of(result));
    }

    private UUID getCurrentUserId() {
        // Extracted from SecurityContext JWT claims
        return UUID.fromString(org.springframework.security.core.context.SecurityContextHolder
                .getContext().getAuthentication().getName());
    }
}
