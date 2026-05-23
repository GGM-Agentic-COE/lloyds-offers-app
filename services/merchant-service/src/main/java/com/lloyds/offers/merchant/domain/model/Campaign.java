package com.lloyds.offers.merchant.domain.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "campaigns")
public class Campaign {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private UUID merchantId;
    private String title;
    private String description;
    private String terms;
    private String imageUrl;
    private String discountType;
    private BigDecimal discountValue;

    @ElementCollection
    @CollectionTable(name = "campaign_zone_ids", joinColumns = @JoinColumn(name = "campaign_id"))
    @Column(name = "zone_id")
    private List<String> zoneIds;

    private LocalTime activeHoursStart;
    private LocalTime activeHoursEnd;

    @ElementCollection
    @CollectionTable(name = "campaign_target_categories", joinColumns = @JoinColumn(name = "campaign_id"))
    @Column(name = "category")
    private List<String> targetCategories;

    private Integer maxRedemptions;
    private Integer maxPerUser;
    private Instant validFrom;
    private Instant validTo;

    @Enumerated(EnumType.STRING)
    private CampaignStatus status;

    private Instant publishedAt;

    public enum CampaignStatus { DRAFT, ACTIVE, PAUSED, EXPIRED }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
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
    public String getDiscountType() { return discountType; }
    public void setDiscountType(String discountType) { this.discountType = discountType; }
    public BigDecimal getDiscountValue() { return discountValue; }
    public void setDiscountValue(BigDecimal discountValue) { this.discountValue = discountValue; }
    public List<String> getZoneIds() { return zoneIds; }
    public void setZoneIds(List<String> zoneIds) { this.zoneIds = zoneIds; }
    public LocalTime getActiveHoursStart() { return activeHoursStart; }
    public void setActiveHoursStart(LocalTime activeHoursStart) { this.activeHoursStart = activeHoursStart; }
    public LocalTime getActiveHoursEnd() { return activeHoursEnd; }
    public void setActiveHoursEnd(LocalTime activeHoursEnd) { this.activeHoursEnd = activeHoursEnd; }
    public List<String> getTargetCategories() { return targetCategories; }
    public void setTargetCategories(List<String> targetCategories) { this.targetCategories = targetCategories; }
    public Integer getMaxRedemptions() { return maxRedemptions; }
    public void setMaxRedemptions(Integer maxRedemptions) { this.maxRedemptions = maxRedemptions; }
    public Integer getMaxPerUser() { return maxPerUser; }
    public void setMaxPerUser(Integer maxPerUser) { this.maxPerUser = maxPerUser; }
    public Instant getValidFrom() { return validFrom; }
    public void setValidFrom(Instant validFrom) { this.validFrom = validFrom; }
    public Instant getValidTo() { return validTo; }
    public void setValidTo(Instant validTo) { this.validTo = validTo; }
    public CampaignStatus getStatus() { return status; }
    public void setStatus(CampaignStatus status) { this.status = status; }
    public Instant getPublishedAt() { return publishedAt; }
    public void setPublishedAt(Instant publishedAt) { this.publishedAt = publishedAt; }
}
