package com.lloyds.offers.notification.application.command;

import com.lloyds.offers.notification.domain.model.DeviceToken;
import com.lloyds.offers.notification.domain.model.NotificationLog;
import com.lloyds.offers.notification.domain.port.DeviceTokenRepository;
import com.lloyds.offers.notification.domain.port.NotificationLogRepository;
import com.lloyds.offers.notification.infrastructure.cache.FrequencyCapService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SendNotificationHandlerTest {

    @Mock private DeviceTokenRepository deviceTokenRepository;
    @Mock private NotificationLogRepository notificationLogRepository;
    @Mock private FrequencyCapService frequencyCapService;

    private SendNotificationHandler handler;

    @BeforeEach
    void setUp() {
        handler = new SendNotificationHandler(deviceTokenRepository, notificationLogRepository, frequencyCapService);
    }

    @Test
    void shouldSendNotificationWhenAllowed() {
        NotificationLog notification = buildNotification();
        DeviceToken device = new DeviceToken();
        device.setPlatform(DeviceToken.Platform.ANDROID);
        device.setPushToken("token123");

        when(frequencyCapService.isAllowed("user1", "offer1")).thenReturn(true);
        when(deviceTokenRepository.findByUserIdAndIsActiveTrue("user1")).thenReturn(List.of(device));
        when(notificationLogRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        NotificationLog result = handler.handle(notification);

        assertThat(result.getStatus()).isEqualTo(NotificationLog.Status.SENT);
        verify(frequencyCapService).record("user1", "offer1");
    }

    @Test
    void shouldFailWhenFrequencyCapExceeded() {
        NotificationLog notification = buildNotification();

        when(frequencyCapService.isAllowed("user1", "offer1")).thenReturn(false);
        when(notificationLogRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        NotificationLog result = handler.handle(notification);

        assertThat(result.getStatus()).isEqualTo(NotificationLog.Status.FAILED);
        assertThat(result.getFailureReason()).isEqualTo("Frequency cap exceeded");
        verify(frequencyCapService, never()).record(any(), any());
    }

    @Test
    void shouldFailWhenNoActiveDevices() {
        NotificationLog notification = buildNotification();

        when(frequencyCapService.isAllowed("user1", "offer1")).thenReturn(true);
        when(deviceTokenRepository.findByUserIdAndIsActiveTrue("user1")).thenReturn(List.of());
        when(notificationLogRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        NotificationLog result = handler.handle(notification);

        assertThat(result.getStatus()).isEqualTo(NotificationLog.Status.FAILED);
        assertThat(result.getFailureReason()).isEqualTo("No active devices");
    }

    private NotificationLog buildNotification() {
        NotificationLog n = new NotificationLog();
        n.setUserId("user1");
        n.setOfferId("offer1");
        n.setTitle("Test");
        n.setBody("Test body");
        return n;
    }
}
