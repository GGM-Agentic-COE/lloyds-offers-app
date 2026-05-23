package com.lloyds.offers.notification.application.command;

import com.lloyds.offers.notification.domain.model.DeviceToken;
import com.lloyds.offers.notification.domain.model.NotificationLog;
import com.lloyds.offers.notification.domain.port.DeviceTokenRepository;
import com.lloyds.offers.notification.domain.port.NotificationLogRepository;
import com.lloyds.offers.notification.infrastructure.cache.FrequencyCapService;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class SendNotificationHandler {

    private final DeviceTokenRepository deviceTokenRepository;
    private final NotificationLogRepository notificationLogRepository;
    private final FrequencyCapService frequencyCapService;

    public SendNotificationHandler(DeviceTokenRepository deviceTokenRepository,
                                   NotificationLogRepository notificationLogRepository,
                                   FrequencyCapService frequencyCapService) {
        this.deviceTokenRepository = deviceTokenRepository;
        this.notificationLogRepository = notificationLogRepository;
        this.frequencyCapService = frequencyCapService;
    }

    public NotificationLog handle(NotificationLog notification) {
        String userId = notification.getUserId();
        String offerId = notification.getOfferId();

        if (!frequencyCapService.isAllowed(userId, offerId)) {
            notification.setStatus(NotificationLog.Status.FAILED);
            notification.setFailureReason("Frequency cap exceeded");
            return notificationLogRepository.save(notification);
        }

        List<DeviceToken> devices = deviceTokenRepository.findByUserIdAndIsActiveTrue(userId);
        if (devices.isEmpty()) {
            notification.setStatus(NotificationLog.Status.FAILED);
            notification.setFailureReason("No active devices");
            return notificationLogRepository.save(notification);
        }

        for (DeviceToken device : devices) {
            dispatchPush(device, notification);
        }

        notification.setStatus(NotificationLog.Status.SENT);
        notification.setSentAt(Instant.now());
        frequencyCapService.record(userId, offerId);
        return notificationLogRepository.save(notification);
    }

    private void dispatchPush(DeviceToken device, NotificationLog notification) {
        switch (device.getPlatform()) {
            case IOS -> sendApns(device.getPushToken(), notification);
            case ANDROID -> sendFcm(device.getPushToken(), notification);
        }
    }

    private void sendApns(String token, NotificationLog notification) {
        // TODO: APNs integration
    }

    private void sendFcm(String token, NotificationLog notification) {
        // TODO: FCM integration
    }
}
