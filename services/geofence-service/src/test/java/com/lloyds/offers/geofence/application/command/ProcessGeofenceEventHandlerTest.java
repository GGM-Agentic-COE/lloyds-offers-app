package com.lloyds.offers.geofence.application.command;

import com.lloyds.offers.geofence.domain.model.City;
import com.lloyds.offers.geofence.domain.model.GeofenceEvent;
import com.lloyds.offers.geofence.domain.model.GeofenceZone;
import com.lloyds.offers.geofence.domain.port.ZoneRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import java.time.Instant;
import java.time.LocalTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProcessGeofenceEventHandlerTest {

    @Mock private ZoneRepository zoneRepository;
    @Mock private ProcessGeofenceEventHandler.CityRepository cityRepository;
    @Mock private ProcessGeofenceEventHandler.EventRepository eventRepository;
    @Mock private KafkaTemplate<String, Object> kafkaTemplate;

    private ProcessGeofenceEventHandler handler;

    @BeforeEach
    void setUp() {
        handler = new ProcessGeofenceEventHandler(zoneRepository, cityRepository, eventRepository, kafkaTemplate);
    }

    @Test
    void shouldProcessEntryEventAndPublish() {
        UUID zoneId = UUID.randomUUID();
        UUID cityId = UUID.randomUUID();

        GeofenceZone zone = new GeofenceZone();
        zone.setId(zoneId);
        zone.setCityId(cityId);
        zone.setStatus(GeofenceZone.ZoneStatus.ACTIVE);

        City city = new City();
        city.setId(cityId);
        city.setStatus(City.CityStatus.ACTIVE);

        GeofenceEvent event = new GeofenceEvent();
        event.setZoneId(zoneId);
        event.setUserId(UUID.randomUUID());
        event.setEventType(GeofenceEvent.EventType.ENTRY);
        event.setTimestamp(Instant.now());

        when(zoneRepository.findById(zoneId)).thenReturn(Optional.of(zone));
        when(cityRepository.findById(cityId)).thenReturn(Optional.of(city));
        when(eventRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        GeofenceEvent result = handler.handle(event);

        assertThat(result.getProcessed()).isTrue();
        verify(kafkaTemplate).send(eq("geofence.entry"), any());
    }

    @Test
    void shouldRejectEventWhenCityNotActive() {
        UUID zoneId = UUID.randomUUID();
        UUID cityId = UUID.randomUUID();

        GeofenceZone zone = new GeofenceZone();
        zone.setId(zoneId);
        zone.setCityId(cityId);

        City city = new City();
        city.setId(cityId);
        city.setStatus(City.CityStatus.COMING_SOON);

        GeofenceEvent event = new GeofenceEvent();
        event.setZoneId(zoneId);
        event.setEventType(GeofenceEvent.EventType.ENTRY);
        event.setTimestamp(Instant.now());

        when(zoneRepository.findById(zoneId)).thenReturn(Optional.of(zone));
        when(cityRepository.findById(cityId)).thenReturn(Optional.of(city));

        assertThatThrownBy(() -> handler.handle(event))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("City is not active");
    }

    @Test
    void shouldRejectEventWhenZoneNotFound() {
        GeofenceEvent event = new GeofenceEvent();
        event.setZoneId(UUID.randomUUID());

        when(zoneRepository.findById(any())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> handler.handle(event))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Zone not found");
    }
}
