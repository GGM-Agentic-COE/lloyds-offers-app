package com.lloyds.offers.merchant.domain.port;

import com.lloyds.offers.merchant.domain.model.Campaign;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface CampaignRepository extends JpaRepository<Campaign, UUID> {
    List<Campaign> findByMerchantId(UUID merchantId);
}
