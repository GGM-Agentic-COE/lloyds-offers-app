package com.lloyds.offers.auth.application.command;

import com.lloyds.offers.auth.application.dto.RegisterResponse;
import com.lloyds.offers.auth.domain.model.OtpCode;
import com.lloyds.offers.auth.domain.model.User;
import com.lloyds.offers.auth.domain.port.OtpRepository;
import com.lloyds.offers.auth.domain.port.UserRepository;
import com.lloyds.offers.auth.infrastructure.messaging.publisher.EventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.HexFormat;
import java.util.UUID;

@Service
public class RegisterUserHandler {

    private final UserRepository userRepository;
    private final OtpRepository otpRepository;
    private final EventPublisher eventPublisher;
    private final RateLimiter rateLimiter;

    public RegisterUserHandler(UserRepository userRepository, OtpRepository otpRepository,
                               EventPublisher eventPublisher, RateLimiter rateLimiter) {
        this.userRepository = userRepository;
        this.otpRepository = otpRepository;
        this.eventPublisher = eventPublisher;
        this.rateLimiter = rateLimiter;
    }

    @Transactional
    public RegisterResponse handle(RegisterCommand command) {
        rateLimiter.checkRegistration(command.deviceId());

        userRepository.findByIdentifier(command.identifier()).ifPresent(u -> {
            throw new DuplicateAccountException("Account already exists");
        });

        var user = new User(command.identifier(),
                User.IdentifierType.valueOf(command.identifierType().toUpperCase()));
        user = userRepository.save(user);

        String code = generateOtp();
        String hash = sha256(code);
        var otp = new OtpCode(user.getId(), hash, Instant.now().plusSeconds(90));
        otpRepository.save(otp);

        eventPublisher.publish("user.registered", new UserRegisteredEvent(user.getId(), command.identifierType()));
        // OTP delivery dispatched async via Pub/Sub
        eventPublisher.publish("otp.send", new OtpSendEvent(user.getId(), command.identifier(), command.identifierType(), code));

        return new RegisterResponse(user.getId(), true, 90);
    }

    private String generateOtp() {
        return String.format("%06d", new SecureRandom().nextInt(999999));
    }

    private String sha256(String input) {
        try {
            var digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hash);
        } catch (Exception e) { throw new RuntimeException(e); }
    }

    public record RegisterCommand(String identifier, String identifierType, String deviceId) {}
    public record UserRegisteredEvent(UUID userId, String identifierType) {}
    public record OtpSendEvent(UUID userId, String identifier, String identifierType, String code) {}
}
