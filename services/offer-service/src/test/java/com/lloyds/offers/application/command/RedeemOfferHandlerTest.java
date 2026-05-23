package com.lloyds.offers.application.command;

import com.lloyds.offers.domain.model.Offer;
import com.lloyds.offers.domain.model.Redemption;
import com.lloyds.offers.domain.port.OfferRepository;
import com.lloyds.offers.domain.port.RedemptionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RedeemOfferHandlerTest {

    @Mock
    private OfferRepository offerRepository;
    @Mock
    private RedemptionRepository redemptionRepository;
    @InjectMocks
    private RedeemOfferHandler handler;

    private UUID offerId;
    private UUID userId;
    private Offer offer;

    @BeforeEach
    void setUp() {
        offerId = UUID.randomUUID();
        userId = UUID.randomUUID();
        offer = new Offer();
        offer.setId(offerId);
        offer.setStatus(Offer.Status.ACTIVE);
        offer.setMaxRedemptions(10);
        offer.setMaxPerUser(2);
        offer.setRedemptionCount(0);
    }

    @Test
    void shouldRedeemSuccessfully() {
        when(offerRepository.findById(offerId)).thenReturn(Optional.of(offer));
        when(redemptionRepository.countByOfferIdAndUserId(offerId, userId)).thenReturn(0L);
        when(redemptionRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        when(offerRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        Redemption result = handler.handle(offerId, userId);

        assertThat(result.getOfferId()).isEqualTo(offerId);
        assertThat(result.getUserId()).isEqualTo(userId);
        assertThat(result.getRedemptionToken()).isNotNull();
        assertThat(result.getManualCode()).hasSize(6);
        assertThat(result.getStatus()).isEqualTo(Redemption.Status.PENDING);
        assertThat(result.getTokenExpiresAt()).isNotNull();
        assertThat(offer.getRedemptionCount()).isEqualTo(1);
    }

    @Test
    void shouldRejectWhenOfferNotActive() {
        offer.setStatus(Offer.Status.PAUSED);
        when(offerRepository.findById(offerId)).thenReturn(Optional.of(offer));

        assertThatThrownBy(() -> handler.handle(offerId, userId))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Offer is not active");
    }

    @Test
    void shouldRejectWhenRedemptionLimitReached() {
        offer.setRedemptionCount(10);
        when(offerRepository.findById(offerId)).thenReturn(Optional.of(offer));

        assertThatThrownBy(() -> handler.handle(offerId, userId))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Offer redemption limit reached");
    }

    @Test
    void shouldRejectWhenUserLimitReached() {
        when(offerRepository.findById(offerId)).thenReturn(Optional.of(offer));
        when(redemptionRepository.countByOfferIdAndUserId(offerId, userId)).thenReturn(2L);

        assertThatThrownBy(() -> handler.handle(offerId, userId))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("User redemption limit reached");
    }

    @Test
    void shouldRejectWhenOfferNotFound() {
        when(offerRepository.findById(offerId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> handler.handle(offerId, userId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Offer not found");
    }
}
