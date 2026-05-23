package com.lloyds.offers.merchant.api.controller;

import com.lloyds.offers.merchant.application.command.CreateCampaignHandler;
import com.lloyds.offers.merchant.application.command.PublishCampaignHandler;
import com.lloyds.offers.merchant.domain.model.Campaign;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.bean.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CampaignController.class)
class CampaignControllerContractTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CreateCampaignHandler createCampaignHandler;
    @MockBean
    private PublishCampaignHandler publishCampaignHandler;

    @Test
    void shouldCreateCampaignAndReturn201() throws Exception {
        Campaign campaign = new Campaign();
        campaign.setId(UUID.randomUUID());
        campaign.setTitle("Summer Sale");
        campaign.setStatus(Campaign.CampaignStatus.DRAFT);
        when(createCampaignHandler.handle(any())).thenReturn(campaign);

        mockMvc.perform(post("/api/campaigns")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"Summer Sale\",\"merchantId\":\"" + UUID.randomUUID() + "\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Summer Sale"))
                .andExpect(jsonPath("$.status").value("DRAFT"));
    }

    @Test
    void shouldPublishCampaignAndReturnActive() throws Exception {
        UUID id = UUID.randomUUID();
        Campaign campaign = new Campaign();
        campaign.setId(id);
        campaign.setStatus(Campaign.CampaignStatus.ACTIVE);
        when(publishCampaignHandler.handle(id)).thenReturn(campaign);

        mockMvc.perform(post("/api/campaigns/" + id + "/publish"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("ACTIVE"));
    }
}
