package com.lloyds.offers.geofence.domain.model;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "cities")
public class City {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, length = 2)
    private String countryCode;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CityStatus status = CityStatus.ACTIVE;

    public enum CityStatus { ACTIVE, COMING_SOON }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCountryCode() { return countryCode; }
    public void setCountryCode(String countryCode) { this.countryCode = countryCode; }
    public CityStatus getStatus() { return status; }
    public void setStatus(CityStatus status) { this.status = status; }
}
