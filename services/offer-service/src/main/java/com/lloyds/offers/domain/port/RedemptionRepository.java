package com.lloyds.offers.domain.port;

import com.lloyds.offers.domain.model.Redemption;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface RedemptionRepository extends JpaRepository<Redemption, UUID> {
    long countByOfferIdAndUserId(UUID offerId, UUID userId);
    Optional<Redemption> findByRedemptionToken(UUID token);
}
