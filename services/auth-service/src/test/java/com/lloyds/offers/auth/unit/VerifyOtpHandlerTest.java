package com.lloyds.offers.auth.unit;

import com.lloyds.offers.auth.application.command.*;
import com.lloyds.offers.auth.application.command.VerifyOtpHandler.VerifyOtpCommand;
import com.lloyds.offers.auth.application.dto.TokenResponse;
import com.lloyds.offers.auth.domain.model.OtpCode;
import com.lloyds.offers.auth.domain.model.User;
import com.lloyds.offers.auth.domain.port.OtpRepository;
import com.lloyds.offers.auth.domain.port.UserRepository;
import com.lloyds.offers.auth.infrastructure.messaging.publisher.EventPublisher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VerifyOtpHandlerTest {

    @Mock private OtpRepository otpRepository;
    @Mock private UserRepository userRepository;
    @Mock private TokenService tokenService;
    @Mock private EventPublisher eventPublisher;

    private VerifyOtpHandler handler;
    private UUID userId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        handler = new VerifyOtpHandler(otpRepository, userRepository, tokenService, eventPublisher);
    }

    @Test
    void shouldVerifyCorrectOtp_andIssueTokens() {
        var otp = new OtpCode(userId, sha256("123456"), Instant.now().plusSeconds(60));
        var user = new User("+447700900001", User.IdentifierType.PHONE);
        var tokens = new TokenResponse("access", "refresh", 900);

        when(otpRepository.findLatestUnusedByUserId(userId)).thenReturn(Optional.of(otp));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(tokenService.issueTokens(any(), any())).thenReturn(tokens);

        var result = handler.handle(new VerifyOtpCommand(userId, "123456", "device-1"));

        assertThat(result.accessToken()).isEqualTo("access");
        assertThat(result.tokenType()).isEqualTo("Bearer");
        verify(eventPublisher).publish(eq("user.verified"), any());
    }

    @Test
    void shouldRejectExpiredOtp() {
        var otp = new OtpCode(userId, sha256("123456"), Instant.now().minusSeconds(10));
        when(otpRepository.findLatestUnusedByUserId(userId)).thenReturn(Optional.of(otp));

        assertThatThrownBy(() -> handler.handle(new VerifyOtpCommand(userId, "123456", "d")))
                .isInstanceOf(InvalidOtpException.class)
                .hasMessageContaining("expired");
    }

    @Test
    void shouldRejectWrongCode_andDecrementAttempts() {
        var otp = new OtpCode(userId, sha256("123456"), Instant.now().plusSeconds(60));
        when(otpRepository.findLatestUnusedByUserId(userId)).thenReturn(Optional.of(otp));

        assertThatThrownBy(() -> handler.handle(new VerifyOtpCommand(userId, "000000", "d")))
                .isInstanceOf(InvalidOtpException.class)
                .hasMessageContaining("Invalid");

        verify(otpRepository).save(otp); // attempts incremented
    }

    @Test
    void shouldLockAfter5FailedAttempts() {
        var otp = new OtpCode(userId, sha256("123456"), Instant.now().plusSeconds(60));
        // Simulate 5 prior attempts
        for (int i = 0; i < 5; i++) otp.incrementAttempts();
        when(otpRepository.findLatestUnusedByUserId(userId)).thenReturn(Optional.of(otp));

        assertThatThrownBy(() -> handler.handle(new VerifyOtpCommand(userId, "123456", "d")))
                .isInstanceOf(OtpLockedException.class);
    }

    private String sha256(String input) {
        try {
            var digest = java.security.MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes(java.nio.charset.StandardCharsets.UTF_8));
            return java.util.HexFormat.of().formatHex(hash);
        } catch (Exception e) { throw new RuntimeException(e); }
    }
}
