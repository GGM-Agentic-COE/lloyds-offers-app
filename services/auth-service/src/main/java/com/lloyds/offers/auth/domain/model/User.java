package com.lloyds.offers.auth.domain.model;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String identifier;

    @Column(name = "identifier_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private IdentifierType identifierType;

    @Column(name = "password_hash")
    private String passwordHash;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserStatus status = UserStatus.PENDING;

    @Column(name = "biometric_enabled", nullable = false)
    private boolean biometricEnabled = false;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    @Column(name = "verified_at")
    private Instant verifiedAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt = Instant.now();

    @Version
    private int version;

    protected User() {}

    public User(String identifier, IdentifierType identifierType) {
        this.identifier = identifier;
        this.identifierType = identifierType;
    }

    public void verify() {
        this.status = UserStatus.VERIFIED;
        this.verifiedAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    public UUID getId() { return id; }
    public String getIdentifier() { return identifier; }
    public IdentifierType getIdentifierType() { return identifierType; }
    public UserStatus getStatus() { return status; }
    public boolean isBiometricEnabled() { return biometricEnabled; }
    public void setBiometricEnabled(boolean enabled) { this.biometricEnabled = enabled; this.updatedAt = Instant.now(); }
    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String hash) { this.passwordHash = hash; this.updatedAt = Instant.now(); }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getVerifiedAt() { return verifiedAt; }

    public enum IdentifierType { PHONE, EMAIL }
    public enum UserStatus { PENDING, VERIFIED, SUSPENDED, PENDING_DELETION, DELETED }
}
