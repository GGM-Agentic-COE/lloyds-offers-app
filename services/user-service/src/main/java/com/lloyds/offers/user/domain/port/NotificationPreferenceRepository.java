package com.lloyds.offers.user.domain.port;

import com.lloyds.offers.user.domain.model.NotificationPreference;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface NotificationPreferenceRepository extends JpaRepository<NotificationPreference, String> {
    Optional<NotificationPreference> findByUserId(String userId);
    void deleteByUserId(String userId);
}
