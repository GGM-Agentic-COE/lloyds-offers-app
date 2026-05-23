package com.lloyds.offers.auth.application.command;

import com.lloyds.offers.auth.application.dto.TokenResponse;
import com.lloyds.offers.auth.domain.model.OtpCode;
import com.lloyds.offers.auth.domain.model.User;
import com.lloyds.offers.auth.domain.port.OtpRepository;
import com.lloyds.offers.auth.domain.port.UserRepository;
import com.lloyds.offers.auth.infrastructure.messaging.publisher.EventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.HexFormat;
import java.util.UUID;

@Service
public class VerifyOtpHandler {

    private final OtpRepository otpRepository;
    private final UserRepository userRepository;
    private final TokenService tokenService;
    private final EventPublisher eventPublisher;

    public VerifyOtpHandler(OtpRepository otpRepository, UserRepository userRepository,
                            TokenService tokenService, EventPublisher eventPublisher) {
        this.otpRepository = otpRepository;
        this.userRepository = userRepository;
        this.tokenService = tokenService;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public TokenResponse handle(VerifyOtpCommand command) {
        var otp = otpRepository.findLatestUnusedByUserId(command.userId())
                .orElseThrow(() -> new InvalidOtpException("Code expired", 0));

        if (otp.isExpired()) throw new InvalidOtpException("Code expired", otp.getAttemptsRemaining());
        if (otp.isLocked()) throw new OtpLockedException();

        otp.incrementAttempts();
        String inputHash = sha256(command.code());

        if (!inputHash.equals(otp.getCodeHash())) {
            otpRepository.save(otp);
            throw new InvalidOtpException("Invalid code", otp.getAttemptsRemaining());
        }

        otp.markUsed();
        otpRepository.save(otp);

        var user = userRepository.findById(command.userId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.verify();
        userRepository.save(user);

        var tokens = tokenService.issueTokens(user.getId(), command.deviceId());
        eventPublisher.publish("user.verified", new UserVerifiedEvent(user.getId()));

        return tokens;
    }

    private String sha256(String input) {
        try {
            var digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hash);
        } catch (Exception e) { throw new RuntimeException(e); }
    }

    public record VerifyOtpCommand(UUID userId, String code, String deviceId) {}
    public record UserVerifiedEvent(UUID userId) {}
}
