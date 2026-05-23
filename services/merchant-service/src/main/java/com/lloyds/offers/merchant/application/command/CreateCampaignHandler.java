package com.lloyds.offers.merchant.application.command;

import com.lloyds.offers.merchant.domain.model.Campaign;
import com.lloyds.offers.merchant.domain.port.CampaignRepository;
import org.springframework.stereotype.Service;

@Service
public class CreateCampaignHandler {

    private final CampaignRepository campaignRepository;

    public CreateCampaignHandler(CampaignRepository campaignRepository) {
        this.campaignRepository = campaignRepository;
    }

    public Campaign handle(Campaign campaign) {
        campaign.setStatus(Campaign.CampaignStatus.DRAFT);
        return campaignRepository.save(campaign);
    }
}
