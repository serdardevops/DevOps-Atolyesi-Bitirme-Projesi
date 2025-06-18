package com.devops.atolyesi.controller;

import com.devops.atolyesi.model.DevOpsInfo;
import com.devops.atolyesi.service.DevOpsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * DevOps Atolyesi REST API Controller
 * 
 * @author Serdar Selçuk
 */
@RestController
@RequestMapping("/api/v1")
@Tag(name = "DevOps API", description = "DevOps Atolyesi Bitirme Projesi REST API")
@Validated
public class DevOpsController {
    
    @Autowired
    private DevOpsService devOpsService;
    
    @GetMapping("/")
    @Operation(summary = "Ana Sayfa", description = "Uygulama ana sayfa bilgileri")
    public ResponseEntity<Map<String, Object>> home() {
        return ResponseEntity.ok(devOpsService.getApplicationInfo());
    }
    
    @GetMapping("/health")
    @Operation(summary = "Sağlık Kontrolü", description = "Uygulama sağlık durumu")
    public ResponseEntity<Map<String, String>> health() {
        return ResponseEntity.ok(Map.of(
            "status", "UP",
            "message", "DevOps Atolyesi Application is running!",
            "timestamp", java.time.Instant.now().toString()
        ));
    }
    
    @GetMapping("/info")
    @Operation(summary = "Uygulama Bilgileri", description = "Detaylı uygulama bilgileri")
    public ResponseEntity<DevOpsInfo> info() {
        return ResponseEntity.ok(devOpsService.getDevOpsInfo());
    }
    
    @GetMapping("/tools")
    @Operation(summary = "DevOps Araçları", description = "Projede kullanılan DevOps araçları listesi")
    public ResponseEntity<List<Map<String, String>>> tools() {
        return ResponseEntity.ok(devOpsService.getDevOpsTools());
    }
    
    @GetMapping("/pipeline")
    @Operation(summary = "Pipeline Durumu", description = "CI/CD Pipeline durumu ve aşamaları")
    public ResponseEntity<Map<String, Object>> pipeline() {
        return ResponseEntity.ok(devOpsService.getPipelineStatus());
    }
    
    @PostMapping("/deploy")
    @Operation(summary = "Deployment Simülasyonu", description = "Deployment işlemini simüle eder")
    public ResponseEntity<Map<String, String>> deploy(@RequestParam(defaultValue = "production") String environment) {
        return ResponseEntity.ok(devOpsService.simulateDeployment(environment));
    }
    
    @GetMapping("/metrics")
    @Operation(summary = "Sistem Metrikleri", description = "Temel sistem metrikleri")
    public ResponseEntity<Map<String, Object>> metrics() {
        return ResponseEntity.ok(devOpsService.getSystemMetrics());
    }
    
    @GetMapping("/version")
    @Operation(summary = "Sürüm Bilgisi", description = "Uygulama sürüm bilgileri")
    public ResponseEntity<Map<String, String>> version() {
        return ResponseEntity.ok(Map.of(
            "version", "1.0.0",
            "buildTime", "2025-06-18T14:00:00Z",
            "gitCommit", "latest",
            "javaVersion", System.getProperty("java.version"),
            "profile", devOpsService.getActiveProfile()
        ));
    }
} 