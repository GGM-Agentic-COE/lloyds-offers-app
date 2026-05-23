package com.lloyds.offers.merchant.api.controller;

import com.lloyds.offers.merchant.application.command.CreateCampaignHandler;
import com.lloyds.offers.merchant.application.command.PublishCampaignHandler;
import com.lloyds.offers.merchant.domain.model.Campaign;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/campaigns")
public class CampaignController {

    private final CreateCampaignHandler createCampaignHandler;
    private final PublishCampaignHandler publishCampaignHandler;

    public CampaignController(CreateCampaignHandler createCampaignHandler, PublishCampaignHandler publishCampaignHandler) {
        this.createCampaignHandler = createCampaignHandler;
        this.publishCampaignHandler = publishCampaignHandler;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Campaign create(@RequestBody Campaign campaign) {
        return createCampaignHandler.handle(campaign);
    }

    @PostMapping("/{id}/publish")
    public Campaign publish(@PathVariable UUID id) {
        return publishCampaignHandler.handle(id);
    }
}
