package com.lloyds.offers.auth.contract;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AuthApiContractTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void register_shouldReturn201_withValidRequest() throws Exception {
        mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {"identifier": "+447700900001", "identifierType": "phone", "deviceId": "test-device"}
                    """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.userId").exists())
                .andExpect(jsonPath("$.data.verificationRequired").value(true))
                .andExpect(jsonPath("$.data.expiresIn").value(90));
    }

    @Test
    void register_shouldReturn400_withInvalidIdentifierType() throws Exception {
        mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {"identifier": "+447700900001", "identifierType": "fax", "deviceId": "test"}
                    """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void register_shouldReturn409_withDuplicateIdentifier() throws Exception {
        // First registration
        mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {"identifier": "+447700900099", "identifierType": "phone", "deviceId": "test"}
                    """))
                .andExpect(status().isCreated());

        // Duplicate
        mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {"identifier": "+447700900099", "identifierType": "phone", "deviceId": "test"}
                    """))
                .andExpect(status().isConflict());
    }

    @Test
    void verifyOtp_shouldReturn401_withWrongCode() throws Exception {
        mockMvc.perform(post("/api/v1/auth/verify-otp")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {"userId": "00000000-0000-0000-0000-000000000001", "code": "000000"}
                    """))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void health_shouldReturn200() throws Exception {
        mockMvc.perform(get("/actuator/health/liveness"))
                .andExpect(status().isOk());
    }

    @Test
    void readiness_shouldReturn200() throws Exception {
        mockMvc.perform(get("/actuator/health/readiness"))
                .andExpect(status().isOk());
    }
}
