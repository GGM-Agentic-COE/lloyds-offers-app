package com.lloyds.offers.geofence.domain.port;

import com.lloyds.offers.geofence.domain.model.GeofenceZone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.UUID;

public interface ZoneRepository extends JpaRepository<GeofenceZone, UUID> {

    List<GeofenceZone> findByMerchantId(UUID merchantId);

    @Query(value = """
        SELECT * FROM geofence_zones z
        WHERE z.status = 'ACTIVE'
        AND ST_DWithin(
            ST_MakePoint(z.center_lng, z.center_lat)::geography,
            ST_MakePoint(:lng, :lat)::geography,
            z.radius_meters
        )
        """, nativeQuery = true)
    List<GeofenceZone> findNearbyZones(@Param("lat") double lat, @Param("lng") double lng);
}
