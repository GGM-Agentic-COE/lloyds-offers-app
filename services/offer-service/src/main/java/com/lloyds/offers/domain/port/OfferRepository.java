package com.lloyds.offers.domain.port;

import com.lloyds.offers.domain.model.Offer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface OfferRepository extends JpaRepository<Offer, UUID> {

    @Query("SELECT o FROM Offer o WHERE o.status = 'ACTIVE' " +
           "AND o.centerLat BETWEEN :minLat AND :maxLat " +
           "AND o.centerLng BETWEEN :minLng AND :maxLng")
    List<Offer> findActiveInBoundingBox(@Param("minLat") double minLat,
                                        @Param("maxLat") double maxLat,
                                        @Param("minLng") double minLng,
                                        @Param("maxLng") double maxLng);
}
