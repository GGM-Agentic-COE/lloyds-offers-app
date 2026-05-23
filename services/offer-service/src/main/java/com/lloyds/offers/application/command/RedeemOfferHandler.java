package com.lloyds.offers.application.command;

import com.lloyds.offers.domain.model.Offer;
import com.lloyds.offers.domain.model.Redemption;
import com.lloyds.offers.domain.port.OfferRepository;
import com.lloyds.offers.domain.port.RedemptionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
public class RedeemOfferHandler {

    private final OfferRepository offerRepository;
    private final RedemptionRepository redemptionRepository;

    public RedeemOfferHandler(OfferRepository offerRepository, RedemptionRepository redemptionRepository) {
        this.offerRepository = offerRepository;
        this.redemptionRepository = redemptionRepository;
    }

    @Transactional
    public Redemption handle(UUID offerId, UUID userId) {
        Offer offer = offerRepository.findById(offerId)
                .orElseThrow(() -> new IllegalArgumentException("Offer not found"));

        if (offer.getStatus() != Offer.Status.ACTIVE) {
            throw new IllegalStateException("Offer is not active");
        }
        if (offer.getMaxRedemptions() != null && offer.getRedemptionCount() >= offer.getMaxRedemptions()) {
            throw new IllegalStateException("Offer redemption limit reached");
        }
        if (offer.getMaxPerUser() != null) {
            long userCount = redemptionRepository.countByOfferIdAndUserId(offerId, userId);
            if (userCount >= offer.getMaxPerUser()) {
                throw new IllegalStateException("User redemption limit reached");
            }
        }

        Redemption redemption = new Redemption();
        redemption.setOfferId(offerId);
        redemption.setUserId(userId);
        redemption.setRedemptionToken(UUID.randomUUID());
        redemption.setManualCode(String.format("%06d", (int) (Math.random() * 999999)));
        redemption.setStatus(Redemption.Status.PENDING);
        redemption.setTokenExpiresAt(Instant.now().plus(15, ChronoUnit.MINUTES));

        offer.setRedemptionCount(offer.getRedemptionCount() + 1);
        offerRepository.save(offer);

        return redemptionRepository.save(redemption);
    }
}
