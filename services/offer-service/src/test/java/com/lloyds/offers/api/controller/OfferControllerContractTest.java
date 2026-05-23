package com.lloyds.offers.api.controller;

import com.lloyds.offers.application.command.ConfirmRedemptionHandler;
import com.lloyds.offers.application.command.RedeemOfferHandler;
import com.lloyds.offers.application.query.GetNearbyOffersHandler;
import com.lloyds.offers.domain.model.Offer;
import com.lloyds.offers.domain.port.OfferRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.bean.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OfferController.class)
class OfferControllerContractTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OfferRepository offerRepository;
    @MockBean
    private RedeemOfferHandler redeemOfferHandler;
    @MockBean
    private ConfirmRedemptionHandler confirmRedemptionHandler;
    @MockBean
    private GetNearbyOffersHandler getNearbyOffersHandler;

    @Test
    void getAllOffers_returnsOkWithList() throws Exception {
        Offer offer = new Offer();
        offer.setTitle("10% off coffee");
        offer.setStatus(Offer.Status.ACTIVE);
        when(offerRepository.findAll()).thenReturn(List.of(offer));

        mockMvc.perform(get("/api/v1/offers"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].title").value("10% off coffee"))
                .andExpect(jsonPath("$[0].status").value("ACTIVE"));
    }

    @Test
    void getOfferById_returnsNotFoundWhenMissing() throws Exception {
        UUID id = UUID.randomUUID();
        when(offerRepository.findById(id)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/offers/{id}", id))
                .andExpect(status().isNotFound());
    }

    @Test
    void getNearbyOffers_returnsOk() throws Exception {
        when(getNearbyOffersHandler.handle(51.5, -0.1, 5.0, 0, 20))
                .thenReturn(List.of());

        mockMvc.perform(get("/api/v1/offers/nearby")
                        .param("lat", "51.5")
                        .param("lng", "-0.1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
}
