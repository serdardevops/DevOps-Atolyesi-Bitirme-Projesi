package com.devops.atolyesi;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * Integration tests for DevOps Atolyesi Application
 * 
 * @author Serdar Selçuk
 */
@SpringBootTest
@ActiveProfiles("test")
class ApplicationTests {

    @Test
    void contextLoads() {
        // This test ensures that the Spring application context loads successfully
    }
} 