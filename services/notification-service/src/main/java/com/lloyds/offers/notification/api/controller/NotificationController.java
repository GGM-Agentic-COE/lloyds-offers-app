package com.lloyds.offers.notification.api.controller;

import com.lloyds.offers.notification.application.command.RegisterDeviceHandler;
import com.lloyds.offers.notification.application.command.SendNotificationHandler;
import com.lloyds.offers.notification.domain.model.DeviceToken;
import com.lloyds.offers.notification.domain.model.NotificationLog;
import com.lloyds.offers.notification.domain.port.NotificationLogRepository;
import com.lloyds.offers.notification.infrastructure.cache.FrequencyCapService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class NotificationController {

    private final RegisterDeviceHandler registerDeviceHandler;
    private final SendNotificationHandler sendNotificationHandler;
    private final NotificationLogRepository notificationLogRepository;
    private final FrequencyCapService frequencyCapService;

    public NotificationController(RegisterDeviceHandler registerDeviceHandler,
                                  SendNotificationHandler sendNotificationHandler,
                                  NotificationLogRepository notificationLogRepository,
                                  FrequencyCapService frequencyCapService) {
        this.registerDeviceHandler = registerDeviceHandler;
        this.sendNotificationHandler = sendNotificationHandler;
        this.notificationLogRepository = notificationLogRepository;
        this.frequencyCapService = frequencyCapService;
    }

    @PostMapping("/devices")
    public ResponseEntity<DeviceToken> registerDevice(@Valid @RequestBody DeviceToken device) {
        return ResponseEntity.ok(registerDeviceHandler.handle(device));
    }

    @DeleteMapping("/devices/{id}")
    public ResponseEntity<Void> deregisterDevice(@PathVariable String id) {
        registerDeviceHandler.deregister(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/notifications/send")
    public ResponseEntity<NotificationLog> send(@Valid @RequestBody NotificationLog notification) {
        return ResponseEntity.ok(sendNotificationHandler.handle(notification));
    }

    @GetMapping("/notifications/history")
    public ResponseEntity<List<NotificationLog>> history(@RequestParam String userId) {
        return ResponseEntity.ok(notificationLogRepository.findByUserIdOrderBySentAtDesc(userId));
    }

    @PostMapping("/notifications/{id}/read")
    public ResponseEntity<Void> markRead(@PathVariable String id) {
        notificationLogRepository.findById(id).ifPresent(n -> {
            n.setStatus(NotificationLog.Status.READ);
            notificationLogRepository.save(n);
        });
        return ResponseEntity.ok().build();
    }

    @GetMapping("/frequency-cap/check")
    public ResponseEntity<Map<String, Boolean>> checkCap(@RequestParam String userId,
                                                         @RequestParam String offerId) {
        return ResponseEntity.ok(Map.of("allowed", frequencyCapService.check(userId, offerId)));
    }
}
