package com.lloyds.offers.merchant.application.query;

import com.lloyds.offers.merchant.domain.port.CampaignRepository;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
public class GetAnalyticsHandler {

    private final CampaignRepository campaignRepository;

    public GetAnalyticsHandler(CampaignRepository campaignRepository) {
        this.campaignRepository = campaignRepository;
    }

    public Map<String, Object> handle(UUID merchantId) {
        long totalCampaigns = campaignRepository.findByMerchantId(merchantId).size();
        return Map.of("merchantId", merchantId, "totalCampaigns", totalCampaigns);
    }
}
