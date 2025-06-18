package com.devops.atolyesi.controller;

import com.devops.atolyesi.service.DevOpsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Unit tests for DevOpsController
 * 
 * @author Serdar Selçuk
 */
@WebMvcTest(DevOpsController.class)
class DevOpsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DevOpsService devOpsService;

    @Test
    void testHome() throws Exception {
        Map<String, Object> mockInfo = new HashMap<>();
        mockInfo.put("applicationName", "DevOps Atolyesi Bitirme Projesi");
        mockInfo.put("version", "1.0.0");
        
        when(devOpsService.getApplicationInfo()).thenReturn(mockInfo);

        mockMvc.perform(get("/api/v1/"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.applicationName").value("DevOps Atolyesi Bitirme Projesi"))
                .andExpect(jsonPath("$.version").value("1.0.0"));
    }

    @Test
    void testHealth() throws Exception {
        mockMvc.perform(get("/api/v1/health"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("UP"))
                .andExpect(jsonPath("$.message").value("DevOps Atolyesi Application is running!"));
    }

    @Test
    void testVersion() throws Exception {
        when(devOpsService.getActiveProfile()).thenReturn("test");

        mockMvc.perform(get("/api/v1/version"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.version").value("1.0.0"))
                .andExpect(jsonPath("$.profile").value("test"));
    }

    @Test
    void testDeploy() throws Exception {
        Map<String, String> mockDeployment = new HashMap<>();
        mockDeployment.put("status", "SUCCESS");
        mockDeployment.put("environment", "production");
        
        when(devOpsService.simulateDeployment("production")).thenReturn(mockDeployment);

        mockMvc.perform(post("/api/v1/deploy")
                .param("environment", "production"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andExpect(jsonPath("$.environment").value("production"));
    }

    @Test
    void testMetrics() throws Exception {
        Map<String, Object> mockMetrics = new HashMap<>();
        mockMetrics.put("memoryUsed", "256 MB");
        mockMetrics.put("availableProcessors", 4);
        
        when(devOpsService.getSystemMetrics()).thenReturn(mockMetrics);

        mockMvc.perform(get("/api/v1/metrics"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.memoryUsed").value("256 MB"))
                .andExpect(jsonPath("$.availableProcessors").value(4));
    }
} 