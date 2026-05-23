package com.lloyds.offers.user.api.controller;

import com.lloyds.offers.user.application.command.UpdateProfileHandler;
import com.lloyds.offers.user.application.query.GetProfileHandler;
import com.lloyds.offers.user.domain.model.UserProfile;
import com.lloyds.offers.user.domain.port.NotificationPreferenceRepository;
import com.lloyds.offers.user.domain.port.UserProfileRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerContractTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GetProfileHandler getProfileHandler;
    @MockBean
    private UpdateProfileHandler updateProfileHandler;
    @MockBean
    private UserProfileRepository profileRepository;
    @MockBean
    private NotificationPreferenceRepository notificationRepository;

    @Test
    void getProfile_returnsProfile() throws Exception {
        UserProfile profile = UserProfile.builder()
                .id("id-1").userId("user-1").displayName("Alice")
                .categories(List.of("food")).budgetSensitivity(UserProfile.BudgetSensitivity.MEDIUM)
                .preferredRadiusMeters(5000).createdAt(Instant.now()).updatedAt(Instant.now()).build();

        when(getProfileHandler.handle("user-1")).thenReturn(Optional.of(profile));

        mockMvc.perform(get("/api/v1/users/me/profile")
                        .with(SecurityMockMvcRequestPostProcessors.jwt().jwt(j -> j.subject("user-1"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.displayName").value("Alice"))
                .andExpect(jsonPath("$.budgetSensitivity").value("MEDIUM"));
    }

    @Test
    void getProfile_returns404WhenNotFound() throws Exception {
        when(getProfileHandler.handle("user-2")).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/users/me/profile")
                        .with(SecurityMockMvcRequestPostProcessors.jwt().jwt(j -> j.subject("user-2"))))
                .andExpect(status().isNotFound());
    }

    @Test
    void patchProfile_updatesAndReturnsProfile() throws Exception {
        UserProfile updated = UserProfile.builder()
                .id("id-1").userId("user-1").displayName("Bob")
                .categories(List.of("travel")).budgetSensitivity(UserProfile.BudgetSensitivity.HIGH)
                .preferredRadiusMeters(10000).createdAt(Instant.now()).updatedAt(Instant.now()).build();

        when(updateProfileHandler.handle("user-1", "Bob", null, null, null)).thenReturn(updated);

        mockMvc.perform(patch("/api/v1/users/me/profile")
                        .with(SecurityMockMvcRequestPostProcessors.jwt().jwt(j -> j.subject("user-1")))
                        .contentType("application/json")
                        .content("{\"displayName\":\"Bob\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.displayName").value("Bob"));
    }
}
