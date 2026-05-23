package com.lloyds.offers.domain.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "offers")
public class Offer {

    public enum DiscountType { PERCENTAGE, FIXED, BOGOF }
    public enum Status { ACTIVE, PAUSED, EXPIRED }

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private UUID campaignId;
    private UUID merchantId;
    private String title;
    private String description;
    private String terms;
    private String imageUrl;

    @Enumerated(EnumType.STRING)
    private DiscountType discountType;
    private BigDecimal discountValue;

    private String zoneId;
    private String merchantName;
    private Double centerLat;
    private Double centerLng;
    private Integer maxRedemptions;
    private Integer maxPerUser;
    private Integer redemptionCount = 0;

    @Enumerated(EnumType.STRING)
    private Status status = Status.ACTIVE;

    private Instant validFrom;
    private Instant validTo;

    @ElementCollection
    @CollectionTable(name = "offer_target_categories", joinColumns = @JoinColumn(name = "offer_id"))
    @Column(name = "category")
    private List<String> targetCategories;

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public UUID getCampaignId() { return campaignId; }
    public void setCampaignId(UUID campaignId) { this.campaignId = campaignId; }
    public UUID getMerchantId() { return merchantId; }
    public void setMerchantId(UUID merchantId) { this.merchantId = merchantId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getTerms() { return terms; }
    public void setTerms(String terms) { this.terms = terms; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public DiscountType getDiscountType() { return discountType; }
    public void setDiscountType(DiscountType discountType) { this.discountType = discountType; }
    public BigDecimal getDiscountValue() { return discountValue; }
    public void setDiscountValue(BigDecimal discountValue) { this.discountValue = discountValue; }
    public String getZoneId() { return zoneId; }
    public void setZoneId(String zoneId) { this.zoneId = zoneId; }
    public String getMerchantName() { return merchantName; }
    public void setMerchantName(String merchantName) { this.merchantName = merchantName; }
    public Double getCenterLat() { return centerLat; }
    public void setCenterLat(Double centerLat) { this.centerLat = centerLat; }
    public Double getCenterLng() { return centerLng; }
    public void setCenterLng(Double centerLng) { this.centerLng = centerLng; }
    public Integer getMaxRedemptions() { return maxRedemptions; }
    public void setMaxRedemptions(Integer maxRedemptions) { this.maxRedemptions = maxRedemptions; }
    public Integer getMaxPerUser() { return maxPerUser; }
    public void setMaxPerUser(Integer maxPerUser) { this.maxPerUser = maxPerUser; }
    public Integer getRedemptionCount() { return redemptionCount; }
    public void setRedemptionCount(Integer redemptionCount) { this.redemptionCount = redemptionCount; }
    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }
    public Instant getValidFrom() { return validFrom; }
    public void setValidFrom(Instant validFrom) { this.validFrom = validFrom; }
    public Instant getValidTo() { return validTo; }
    public void setValidTo(Instant validTo) { this.validTo = validTo; }
    public List<String> getTargetCategories() { return targetCategories; }
    public void setTargetCategories(List<String> targetCategories) { this.targetCategories = targetCategories; }
}
