package com.lloyds.offers.geofence.application.command;

import com.lloyds.offers.geofence.domain.model.GeofenceZone;
import com.lloyds.offers.geofence.domain.port.ZoneRepository;
import org.springframework.stereotype.Service;

@Service
public class CreateZoneHandler {

    private final ZoneRepository zoneRepository;

    public CreateZoneHandler(ZoneRepository zoneRepository) {
        this.zoneRepository = zoneRepository;
    }

    public GeofenceZone handle(GeofenceZone zone) {
        if (zone.getRadiusMeters() < 50 || zone.getRadiusMeters() > 5000) {
            throw new IllegalArgumentException("Radius must be between 50 and 5000 meters");
        }
        return zoneRepository.save(zone);
    }
}
