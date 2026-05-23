package com.lloyds.offers.geofence.api.controller;

import com.lloyds.offers.geofence.application.command.CreateZoneHandler;
import com.lloyds.offers.geofence.application.command.ProcessGeofenceEventHandler;
import com.lloyds.offers.geofence.application.query.GetNearbyZonesHandler;
import com.lloyds.offers.geofence.domain.model.City;
import com.lloyds.offers.geofence.domain.model.GeofenceZone;
import com.lloyds.offers.geofence.domain.port.ZoneRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.bean.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GeofenceController.class)
class GeofenceControllerContractTest {

    @Autowired private MockMvc mockMvc;
    @MockBean private CreateZoneHandler createZoneHandler;
    @MockBean private ProcessGeofenceEventHandler processEventHandler;
    @MockBean private GetNearbyZonesHandler getNearbyZonesHandler;
    @MockBean private ZoneRepository zoneRepository;
    @MockBean private ProcessGeofenceEventHandler.CityRepository cityRepository;

    @Test
    void shouldReturnCities() throws Exception {
        City city = new City();
        city.setId(UUID.randomUUID());
        city.setName("London");
        city.setCountryCode("GB");
        city.setStatus(City.CityStatus.ACTIVE);

        when(cityRepository.findAll()).thenReturn(List.of(city));

        mockMvc.perform(get("/api/v1/cities"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("London"))
                .andExpect(jsonPath("$[0].countryCode").value("GB"));
    }

    @Test
    void shouldCreateZone() throws Exception {
        GeofenceZone zone = new GeofenceZone();
        zone.setId(UUID.randomUUID());
        zone.setMerchantId(UUID.randomUUID());
        zone.setCityId(UUID.randomUUID());
        zone.setCenterLat(51.5074);
        zone.setCenterLng(-0.1278);
        zone.setRadiusMeters(500);
        zone.setStatus(GeofenceZone.ZoneStatus.ACTIVE);

        when(createZoneHandler.handle(org.mockito.ArgumentMatchers.any())).thenReturn(zone);

        mockMvc.perform(post("/api/v1/zones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                                "merchantId": "%s",
                                "cityId": "%s",
                                "centerLat": 51.5074,
                                "centerLng": -0.1278,
                                "radiusMeters": 500
                            }
                            """.formatted(zone.getMerchantId(), zone.getCityId())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.radiusMeters").value(500));
    }
}
