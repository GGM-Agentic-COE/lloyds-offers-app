package com.lloyds.offers.notification.api.controller;

import com.lloyds.offers.notification.application.command.RegisterDeviceHandler;
import com.lloyds.offers.notification.application.command.SendNotificationHandler;
import com.lloyds.offers.notification.domain.model.NotificationLog;
import com.lloyds.offers.notification.domain.port.NotificationLogRepository;
import com.lloyds.offers.notification.infrastructure.cache.FrequencyCapService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.bean.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(NotificationController.class)
class NotificationControllerContractTest {

    @Autowired private MockMvc mockMvc;
    @MockBean private RegisterDeviceHandler registerDeviceHandler;
    @MockBean private SendNotificationHandler sendNotificationHandler;
    @MockBean private NotificationLogRepository notificationLogRepository;
    @MockBean private FrequencyCapService frequencyCapService;

    @Test
    void shouldReturnHistoryForUser() throws Exception {
        when(notificationLogRepository.findByUserIdOrderBySentAtDesc("user1"))
                .thenReturn(List.of());

        mockMvc.perform(get("/notifications/history").param("userId", "user1"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void shouldCheckFrequencyCap() throws Exception {
        when(frequencyCapService.check("user1", "offer1")).thenReturn(true);

        mockMvc.perform(get("/frequency-cap/check")
                        .param("userId", "user1")
                        .param("offerId", "offer1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.allowed").value(true));
    }

    @Test
    void shouldSendNotification() throws Exception {
        NotificationLog log = new NotificationLog();
        log.setUserId("user1");
        log.setStatus(NotificationLog.Status.SENT);
        when(sendNotificationHandler.handle(any())).thenReturn(log);

        mockMvc.perform(post("/notifications/send")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {"userId":"user1","offerId":"offer1","title":"Hi","body":"Hello"}
                            """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SENT"));
    }
}
