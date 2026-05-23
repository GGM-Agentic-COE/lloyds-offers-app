package com.lloyds.offers.api.controller;

import com.lloyds.offers.application.command.ConfirmRedemptionHandler;
import com.lloyds.offers.application.command.RedeemOfferHandler;
import com.lloyds.offers.application.query.GetNearbyOffersHandler;
import com.lloyds.offers.domain.model.Offer;
import com.lloyds.offers.domain.model.Redemption;
import com.lloyds.offers.domain.port.OfferRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/offers")
public class OfferController {

    private final OfferRepository offerRepository;
    private final RedeemOfferHandler redeemOfferHandler;
    private final ConfirmRedemptionHandler confirmRedemptionHandler;
    private final GetNearbyOffersHandler getNearbyOffersHandler;

    public OfferController(OfferRepository offerRepository,
                           RedeemOfferHandler redeemOfferHandler,
                           ConfirmRedemptionHandler confirmRedemptionHandler,
                           GetNearbyOffersHandler getNearbyOffersHandler) {
        this.offerRepository = offerRepository;
        this.redeemOfferHandler = redeemOfferHandler;
        this.confirmRedemptionHandler = confirmRedemptionHandler;
        this.getNearbyOffersHandler = getNearbyOffersHandler;
    }

    @GetMapping
    public List<Offer> getAll() {
        return offerRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Offer> getById(@PathVariable UUID id) {
        return offerRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/nearby")
    public List<Offer> getNearby(@RequestParam double lat,
                                 @RequestParam double lng,
                                 @RequestParam(defaultValue = "5.0") double radiusKm,
                                 @RequestParam(defaultValue = "0") int page,
                                 @RequestParam(defaultValue = "20") int size) {
        return getNearbyOffersHandler.handle(lat, lng, radiusKm, page, size);
    }

    @PostMapping
    public Offer create(@RequestBody Offer offer) {
        return offerRepository.save(offer);
    }

    @PostMapping("/{offerId}/redeem")
    public Redemption redeem(@PathVariable UUID offerId, @RequestParam UUID userId) {
        return redeemOfferHandler.handle(offerId, userId);
    }

    @PostMapping("/redemptions/{token}/confirm")
    public Redemption confirm(@PathVariable UUID token) {
        return confirmRedemptionHandler.handle(token);
    }
}
