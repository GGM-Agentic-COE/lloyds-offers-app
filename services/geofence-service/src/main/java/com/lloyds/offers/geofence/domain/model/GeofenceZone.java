package com.lloyds.offers.geofence.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.time.LocalTime;
import java.util.UUID;

@Entity
@Table(name = "geofence_zones")
public class GeofenceZone {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID merchantId;

    @Column(nullable = false)
    private UUID cityId;

    @Column(nullable = false)
    private Double centerLat;

    @Column(nullable = false)
    private Double centerLng;

    @Min(50) @Max(5000)
    @Column(nullable = false)
    private Integer radiusMeters;

    private LocalTime activeHoursStart;
    private LocalTime activeHoursEnd;

    private String daysOfWeek;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ZoneStatus status = ZoneStatus.ACTIVE;

    public enum ZoneStatus { ACTIVE, PAUSED }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public UUID getMerchantId() { return merchantId; }
    public void setMerchantId(UUID merchantId) { this.merchantId = merchantId; }
    public UUID getCityId() { return cityId; }
    public void setCityId(UUID cityId) { this.cityId = cityId; }
    public Double getCenterLat() { return centerLat; }
    public void setCenterLat(Double centerLat) { this.centerLat = centerLat; }
    public Double getCenterLng() { return centerLng; }
    public void setCenterLng(Double centerLng) { this.centerLng = centerLng; }
    public Integer getRadiusMeters() { return radiusMeters; }
    public void setRadiusMeters(Integer radiusMeters) { this.radiusMeters = radiusMeters; }
    public LocalTime getActiveHoursStart() { return activeHoursStart; }
    public void setActiveHoursStart(LocalTime activeHoursStart) { this.activeHoursStart = activeHoursStart; }
    public LocalTime getActiveHoursEnd() { return activeHoursEnd; }
    public void setActiveHoursEnd(LocalTime activeHoursEnd) { this.activeHoursEnd = activeHoursEnd; }
    public String getDaysOfWeek() { return daysOfWeek; }
    public void setDaysOfWeek(String daysOfWeek) { this.daysOfWeek = daysOfWeek; }
    public ZoneStatus getStatus() { return status; }
    public void setStatus(ZoneStatus status) { this.status = status; }
}
