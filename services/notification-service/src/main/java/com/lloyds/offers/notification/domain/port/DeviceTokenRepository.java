package com.lloyds.offers.notification.domain.port;

import com.lloyds.offers.notification.domain.model.DeviceToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeviceTokenRepository extends JpaRepository<DeviceToken, String> {
    List<DeviceToken> findByUserIdAndIsActiveTrue(String userId);
}
