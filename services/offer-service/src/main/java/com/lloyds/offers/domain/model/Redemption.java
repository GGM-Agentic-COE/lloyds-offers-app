package com.lloyds.offers.domain.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "redemptions")
public class Redemption {

    public enum Status { PENDING, CONFIRMED, EXPIRED }

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private UUID offerId;
    private UUID userId;
    private UUID redemptionToken;
    private String manualCode;

    @Enumerated(EnumType.STRING)
    private Status status = Status.PENDING;

    private Instant tokenExpiresAt;
    private Instant confirmedAt;
    private BigDecimal savingsAmount;

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public UUID getOfferId() { return offerId; }
    public void setOfferId(UUID offerId) { this.offerId = offerId; }
    public UUID getUserId() { return userId; }
    public void setUserId(UUID userId) { this.userId = userId; }
    public UUID getRedemptionToken() { return redemptionToken; }
    public void setRedemptionToken(UUID redemptionToken) { this.redemptionToken = redemptionToken; }
    public String getManualCode() { return manualCode; }
    public void setManualCode(String manualCode) { this.manualCode = manualCode; }
    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }
    public Instant getTokenExpiresAt() { return tokenExpiresAt; }
    public void setTokenExpiresAt(Instant tokenExpiresAt) { this.tokenExpiresAt = tokenExpiresAt; }
    public Instant getConfirmedAt() { return confirmedAt; }
    public void setConfirmedAt(Instant confirmedAt) { this.confirmedAt = confirmedAt; }
    public BigDecimal getSavingsAmount() { return savingsAmount; }
    public void setSavingsAmount(BigDecimal savingsAmount) { this.savingsAmount = savingsAmount; }
}
