package com.lloyds.offers.merchant.application.command;

import com.lloyds.offers.merchant.domain.model.Campaign;
import com.lloyds.offers.merchant.domain.port.CampaignRepository;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class PublishCampaignHandler {

    private final CampaignRepository campaignRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public PublishCampaignHandler(CampaignRepository campaignRepository, KafkaTemplate<String, String> kafkaTemplate) {
        this.campaignRepository = campaignRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    public Campaign handle(UUID campaignId) {
        Campaign campaign = campaignRepository.findById(campaignId)
                .orElseThrow(() -> new IllegalArgumentException("Campaign not found"));
        if (campaign.getStatus() != Campaign.CampaignStatus.DRAFT) {
            throw new IllegalStateException("Only DRAFT campaigns can be published");
        }
        campaign.setStatus(Campaign.CampaignStatus.ACTIVE);
        campaign.setPublishedAt(Instant.now());
        Campaign saved = campaignRepository.save(campaign);
        kafkaTemplate.send("campaign.published", campaignId.toString(), "{\"campaignId\":\"" + campaignId + "\"}");
        return saved;
    }
}
