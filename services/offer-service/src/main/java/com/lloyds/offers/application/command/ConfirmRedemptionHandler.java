package com.lloyds.offers.application.command;

import com.lloyds.offers.domain.model.Redemption;
import com.lloyds.offers.domain.port.RedemptionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
public class ConfirmRedemptionHandler {

    private final RedemptionRepository redemptionRepository;

    public ConfirmRedemptionHandler(RedemptionRepository redemptionRepository) {
        this.redemptionRepository = redemptionRepository;
    }

    @Transactional
    public Redemption handle(UUID token) {
        Redemption redemption = redemptionRepository.findByRedemptionToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Redemption not found"));

        if (redemption.getStatus() != Redemption.Status.PENDING) {
            throw new IllegalStateException("Redemption is not pending");
        }
        if (Instant.now().isAfter(redemption.getTokenExpiresAt())) {
            redemption.setStatus(Redemption.Status.EXPIRED);
            redemptionRepository.save(redemption);
            throw new IllegalStateException("Redemption token expired");
        }

        redemption.setStatus(Redemption.Status.CONFIRMED);
        redemption.setConfirmedAt(Instant.now());
        return redemptionRepository.save(redemption);
    }
}
