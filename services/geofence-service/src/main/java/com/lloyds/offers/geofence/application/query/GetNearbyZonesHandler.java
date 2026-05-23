package com.lloyds.offers.geofence.application.query;

import com.lloyds.offers.geofence.domain.model.GeofenceZone;
import com.lloyds.offers.geofence.domain.port.ZoneRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetNearbyZonesHandler {

    private final ZoneRepository zoneRepository;

    public GetNearbyZonesHandler(ZoneRepository zoneRepository) {
        this.zoneRepository = zoneRepository;
    }

    public List<GeofenceZone> handle(double lat, double lng) {
        return zoneRepository.findNearbyZones(lat, lng);
    }
}
