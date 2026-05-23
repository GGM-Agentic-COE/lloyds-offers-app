package com.lloyds.offers.geofence.api.controller;

import com.lloyds.offers.geofence.application.command.CreateZoneHandler;
import com.lloyds.offers.geofence.application.command.ProcessGeofenceEventHandler;
import com.lloyds.offers.geofence.application.query.GetNearbyZonesHandler;
import com.lloyds.offers.geofence.domain.model.City;
import com.lloyds.offers.geofence.domain.model.GeofenceEvent;
import com.lloyds.offers.geofence.domain.model.GeofenceZone;
import com.lloyds.offers.geofence.domain.port.ZoneRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
public class GeofenceController {

    private final CreateZoneHandler createZoneHandler;
    private final ProcessGeofenceEventHandler processEventHandler;
    private final GetNearbyZonesHandler getNearbyZonesHandler;
    private final ZoneRepository zoneRepository;
    private final ProcessGeofenceEventHandler.CityRepository cityRepository;

    public GeofenceController(CreateZoneHandler createZoneHandler,
                              ProcessGeofenceEventHandler processEventHandler,
                              GetNearbyZonesHandler getNearbyZonesHandler,
                              ZoneRepository zoneRepository,
                              ProcessGeofenceEventHandler.CityRepository cityRepository) {
        this.createZoneHandler = createZoneHandler;
        this.processEventHandler = processEventHandler;
        this.getNearbyZonesHandler = getNearbyZonesHandler;
        this.zoneRepository = zoneRepository;
        this.cityRepository = cityRepository;
    }

    @PostMapping("/zones")
    @ResponseStatus(HttpStatus.CREATED)
    public GeofenceZone createZone(@RequestBody GeofenceZone zone) {
        return createZoneHandler.handle(zone);
    }

    @GetMapping("/zones/{id}")
    public GeofenceZone getZone(@PathVariable UUID id) {
        return zoneRepository.findById(id).orElseThrow();
    }

    @GetMapping("/zones/nearby")
    public List<GeofenceZone> getNearbyZones(@RequestParam double lat, @RequestParam double lng) {
        return getNearbyZonesHandler.handle(lat, lng);
    }

    @DeleteMapping("/zones/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteZone(@PathVariable UUID id) {
        zoneRepository.deleteById(id);
    }

    @PostMapping("/events")
    @ResponseStatus(HttpStatus.CREATED)
    public GeofenceEvent processEvent(@RequestBody GeofenceEvent event) {
        return processEventHandler.handle(event);
    }

    @GetMapping("/cities")
    public List<City> getCities() {
        return cityRepository.findAll();
    }
}
