package com.lloyds.offers.geofence.application.command;

import com.lloyds.offers.geofence.domain.model.City;
import com.lloyds.offers.geofence.domain.model.GeofenceEvent;
import com.lloyds.offers.geofence.domain.model.GeofenceZone;
import com.lloyds.offers.geofence.domain.port.ZoneRepository;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalTime;

@Service
public class ProcessGeofenceEventHandler {

    private final ZoneRepository zoneRepository;
    private final CityRepository cityRepository;
    private final EventRepository eventRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public ProcessGeofenceEventHandler(ZoneRepository zoneRepository,
                                       CityRepository cityRepository,
                                       EventRepository eventRepository,
                                       KafkaTemplate<String, Object> kafkaTemplate) {
        this.zoneRepository = zoneRepository;
        this.cityRepository = cityRepository;
        this.eventRepository = eventRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    public GeofenceEvent handle(GeofenceEvent event) {
        GeofenceZone zone = zoneRepository.findById(event.getZoneId())
                .orElseThrow(() -> new IllegalArgumentException("Zone not found"));

        City city = cityRepository.findById(zone.getCityId())
                .orElseThrow(() -> new IllegalArgumentException("City not found"));

        if (city.getStatus() != City.CityStatus.ACTIVE) {
            throw new IllegalStateException("City is not active");
        }

        if (zone.getActiveHoursStart() != null && zone.getActiveHoursEnd() != null) {
            LocalTime now = LocalTime.now();
            if (now.isBefore(zone.getActiveHoursStart()) || now.isAfter(zone.getActiveHoursEnd())) {
                throw new IllegalStateException("Zone is outside active hours");
            }
        }

        GeofenceEvent saved = eventRepository.save(event);

        if (event.getEventType() == GeofenceEvent.EventType.ENTRY) {
            kafkaTemplate.send("geofence.entry", saved);
        }

        saved.setProcessed(true);
        return eventRepository.save(saved);
    }

    public interface CityRepository extends org.springframework.data.jpa.repository.JpaRepository<City, java.util.UUID> {}
    public interface EventRepository extends org.springframework.data.jpa.repository.JpaRepository<GeofenceEvent, java.util.UUID> {}
}
