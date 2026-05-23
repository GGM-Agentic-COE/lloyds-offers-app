package com.lloyds.offers.auth.unit;

import com.lloyds.offers.auth.application.command.RegisterUserHandler;
import com.lloyds.offers.auth.application.command.RegisterUserHandler.RegisterCommand;
import com.lloyds.offers.auth.application.command.DuplicateAccountException;
import com.lloyds.offers.auth.application.command.RateLimiter;
import com.lloyds.offers.auth.domain.model.User;
import com.lloyds.offers.auth.domain.port.OtpRepository;
import com.lloyds.offers.auth.domain.port.UserRepository;
import com.lloyds.offers.auth.infrastructure.messaging.publisher.EventPublisher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegisterUserHandlerTest {

    @Mock private UserRepository userRepository;
    @Mock private OtpRepository otpRepository;
    @Mock private EventPublisher eventPublisher;
    @Mock private RateLimiter rateLimiter;

    private RegisterUserHandler handler;

    @BeforeEach
    void setUp() {
        handler = new RegisterUserHandler(userRepository, otpRepository, eventPublisher, rateLimiter);
    }

    @Test
    void shouldRegisterNewUser_withPhone() {
        var command = new RegisterCommand("+447700900001", "phone", "device-1");
        when(userRepository.findByIdentifier("+447700900001")).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenAnswer(inv -> {
            var user = inv.getArgument(0, User.class);
            return user; // In real impl, ID would be set by DB
        });

        var result = handler.handle(command);

        assertThat(result.verificationRequired()).isTrue();
        assertThat(result.expiresIn()).isEqualTo(90);
        verify(userRepository).save(any(User.class));
        verify(otpRepository).save(any());
        verify(eventPublisher).publish(eq("user.registered"), any());
        verify(eventPublisher).publish(eq("otp.send"), any());
    }

    @Test
    void shouldRejectDuplicateIdentifier() {
        var command = new RegisterCommand("+447700900099", "phone", "device-1");
        when(userRepository.findByIdentifier("+447700900099"))
                .thenReturn(Optional.of(new User("+447700900099", User.IdentifierType.PHONE)));

        assertThatThrownBy(() -> handler.handle(command))
                .isInstanceOf(DuplicateAccountException.class);

        verify(userRepository, never()).save(any());
    }

    @Test
    void shouldEnforceRateLimit() {
        var command = new RegisterCommand("+447700900001", "phone", "rate-limited-device");
        doThrow(new RateLimitExceededException()).when(rateLimiter).checkRegistration("rate-limited-device");

        assertThatThrownBy(() -> handler.handle(command))
                .isInstanceOf(RateLimitExceededException.class);
    }
}
