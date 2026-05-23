package com.lloyds.offers.user.api.controller;

import com.lloyds.offers.user.application.command.UpdateProfileHandler;
import com.lloyds.offers.user.application.query.GetProfileHandler;
import com.lloyds.offers.user.domain.model.NotificationPreference;
import com.lloyds.offers.user.domain.model.UserProfile;
import com.lloyds.offers.user.domain.port.NotificationPreferenceRepository;
import com.lloyds.offers.user.domain.port.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/users/me")
@RequiredArgsConstructor
public class UserController {

    private final GetProfileHandler getProfileHandler;
    private final UpdateProfileHandler updateProfileHandler;
    private final UserProfileRepository profileRepository;
    private final NotificationPreferenceRepository notificationRepository;

    @GetMapping("/profile")
    public ResponseEntity<UserProfile> getProfile(@AuthenticationPrincipal Jwt jwt) {
        return getProfileHandler.handle(jwt.getSubject())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/profile")
    public ResponseEntity<UserProfile> updateProfile(@AuthenticationPrincipal Jwt jwt, @RequestBody Map<String, Object> patch) {
        var profile = updateProfileHandler.handle(
                jwt.getSubject(),
                (String) patch.get("displayName"),
                patch.containsKey("categories") ? (java.util.List<String>) patch.get("categories") : null,
                patch.containsKey("budgetSensitivity") ? UserProfile.BudgetSensitivity.valueOf((String) patch.get("budgetSensitivity")) : null,
                patch.containsKey("preferredRadiusMeters") ? (Integer) patch.get("preferredRadiusMeters") : null
        );
        return ResponseEntity.ok(profile);
    }

    @GetMapping("/preferences/notifications")
    public ResponseEntity<NotificationPreference> getNotifications(@AuthenticationPrincipal Jwt jwt) {
        return notificationRepository.findByUserId(jwt.getSubject())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/preferences/notifications")
    @Transactional
    public ResponseEntity<NotificationPreference> updateNotifications(@AuthenticationPrincipal Jwt jwt, @RequestBody Map<String, Object> patch) {
        NotificationPreference pref = notificationRepository.findByUserId(jwt.getSubject())
                .orElseGet(() -> NotificationPreference.builder().userId(jwt.getSubject()).mode(NotificationPreference.Mode.ALWAYS).build());

        if (patch.containsKey("mode")) pref.setMode(NotificationPreference.Mode.valueOf((String) patch.get("mode")));
        if (patch.containsKey("quietHoursStart")) pref.setQuietHoursStart(java.time.LocalTime.parse((String) patch.get("quietHoursStart")));
        if (patch.containsKey("quietHoursEnd")) pref.setQuietHoursEnd(java.time.LocalTime.parse((String) patch.get("quietHoursEnd")));
        if (patch.containsKey("categories")) pref.setCategories((Map<String, Boolean>) patch.get("categories"));

        return ResponseEntity.ok(notificationRepository.save(pref));
    }

    @DeleteMapping
    @Transactional
    public ResponseEntity<Void> deleteUser(@AuthenticationPrincipal Jwt jwt) {
        profileRepository.deleteByUserId(jwt.getSubject());
        notificationRepository.deleteByUserId(jwt.getSubject());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/export")
    public ResponseEntity<Map<String, Object>> exportData(@AuthenticationPrincipal Jwt jwt) {
        var profile = getProfileHandler.handle(jwt.getSubject()).orElse(null);
        var notifications = notificationRepository.findByUserId(jwt.getSubject()).orElse(null);
        return ResponseEntity.ok(Map.of("profile", profile != null ? profile : Map.of(), "notifications", notifications != null ? notifications : Map.of()));
    }
}
