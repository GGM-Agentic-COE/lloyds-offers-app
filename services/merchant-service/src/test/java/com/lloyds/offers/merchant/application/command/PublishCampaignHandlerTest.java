package com.lloyds.offers.merchant.application.command;

import com.lloyds.offers.merchant.domain.model.Campaign;
import com.lloyds.offers.merchant.domain.port.CampaignRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PublishCampaignHandlerTest {

    @Mock
    private CampaignRepository campaignRepository;
    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;
    @InjectMocks
    private PublishCampaignHandler handler;

    private UUID campaignId;
    private Campaign campaign;

    @BeforeEach
    void setUp() {
        campaignId = UUID.randomUUID();
        campaign = new Campaign();
        campaign.setId(campaignId);
        campaign.setStatus(Campaign.CampaignStatus.DRAFT);
    }

    @Test
    void shouldPublishDraftCampaign() {
        when(campaignRepository.findById(campaignId)).thenReturn(Optional.of(campaign));
        when(campaignRepository.save(any())).thenReturn(campaign);

        Campaign result = handler.handle(campaignId);

        assertEquals(Campaign.CampaignStatus.ACTIVE, result.getStatus());
        assertNotNull(result.getPublishedAt());
        verify(kafkaTemplate).send(eq("campaign.published"), eq(campaignId.toString()), any());
    }

    @Test
    void shouldRejectNonDraftCampaign() {
        campaign.setStatus(Campaign.CampaignStatus.ACTIVE);
        when(campaignRepository.findById(campaignId)).thenReturn(Optional.of(campaign));

        assertThrows(IllegalStateException.class, () -> handler.handle(campaignId));
        verify(kafkaTemplate, never()).send(any(), any(), any());
    }

    @Test
    void shouldThrowWhenCampaignNotFound() {
        when(campaignRepository.findById(campaignId)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> handler.handle(campaignId));
    }
}
