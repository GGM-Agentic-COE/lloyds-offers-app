package com.lloyds.offers.application.query;

import com.lloyds.offers.domain.model.Offer;
import com.lloyds.offers.domain.port.OfferRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class GetNearbyOffersHandler {

    private static final double EARTH_RADIUS_KM = 6371.0;
    private final OfferRepository offerRepository;

    public GetNearbyOffersHandler(OfferRepository offerRepository) {
        this.offerRepository = offerRepository;
    }

    public List<Offer> handle(double lat, double lng, double radiusKm, int page, int size) {
        double latDelta = radiusKm / 111.0;
        double lngDelta = radiusKm / (111.0 * Math.cos(Math.toRadians(lat)));

        List<Offer> candidates = offerRepository.findActiveInBoundingBox(
                lat - latDelta, lat + latDelta, lng - lngDelta, lng + lngDelta);

        return candidates.stream()
                .filter(o -> haversine(lat, lng, o.getCenterLat(), o.getCenterLng()) <= radiusKm)
                .sorted(Comparator.comparingDouble(o -> haversine(lat, lng, o.getCenterLat(), o.getCenterLng())))
                .skip((long) page * size)
                .limit(size)
                .toList();
    }

    private double haversine(double lat1, double lng1, double lat2, double lng2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                 + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                 * Math.sin(dLng / 2) * Math.sin(dLng / 2);
        return EARTH_RADIUS_KM * 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    }
}
