package com.lloyds.offers.notification.domain.port;

import com.lloyds.offers.notification.domain.model.NotificationLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationLogRepository extends JpaRepository<NotificationLog, String> {
    List<NotificationLog> findByUserIdOrderBySentAtDesc(String userId);
}
