package com.lloyds.offers.user.domain.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;
import java.time.LocalTime;
import java.util.Map;

@Entity
@Table(name = "notification_preferences")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class NotificationPreference {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false, unique = true)
    private String userId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Mode mode;

    private LocalTime quietHoursStart;
    private LocalTime quietHoursEnd;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "notification_pref_categories", joinColumns = @JoinColumn(name = "pref_id"))
    @MapKeyColumn(name = "category")
    @Column(name = "enabled")
    private Map<String, Boolean> categories;

    @Column(nullable = false)
    private Instant updatedAt;

    public enum Mode { ALWAYS, APP_OPEN, OFF }

    @PrePersist
    @PreUpdate
    void onSave() { updatedAt = Instant.now(); }
}
