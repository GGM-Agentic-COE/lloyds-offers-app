package com.lloyds.offers.user.domain.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "user_profiles")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class UserProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false, unique = true)
    private String userId;

    private String displayName;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_profile_categories", joinColumns = @JoinColumn(name = "profile_id"))
    @Column(name = "category")
    private List<String> categories;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BudgetSensitivity budgetSensitivity;

    private int preferredRadiusMeters;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private Instant updatedAt;

    public enum BudgetSensitivity { LOW, MEDIUM, HIGH }

    @PrePersist
    void onCreate() { createdAt = updatedAt = Instant.now(); }

    @PreUpdate
    void onUpdate() { updatedAt = Instant.now(); }
}
