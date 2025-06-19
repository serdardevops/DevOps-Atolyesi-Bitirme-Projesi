package com.devops.atolyesi.service;

import com.devops.atolyesi.model.DevOpsInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Arrays;

/**
 * DevOps Atolyesi Business Logic Service
 * 
 * @author Serdar Selçuk
 */
@Service
public class DevOpsService {
    
    @Value("${spring.profiles.active:default}")
    private String activeProfile;
    
    @Value("${app.version:1.0.0}")
    private String appVersion;
    
    public Map<String, Object> getApplicationInfo() {
        Map<String, Object> info = new HashMap<>();
        info.put("applicationName", "DevOps Atolyesi Bitirme Projesi");
        info.put("version", appVersion);
        info.put("author", "Serdar Selçuk");
        info.put("description", "Complete CI/CD Pipeline Demo with Spring Boot");
        info.put("timestamp", Instant.now().toString());
        info.put("profile", activeProfile);
        info.put("javaVersion", System.getProperty("java.version"));
        info.put("osName", System.getProperty("os.name"));
        info.put("osVersion", System.getProperty("os.version"));
        return info;
    }
    
    public DevOpsInfo getDevOpsInfo() {
        DevOpsInfo info = new DevOpsInfo();
        info.setProjectName("DevOps Atolyesi Bitirme Projesi");
        info.setVersion(appVersion);
        info.setAuthor("Serdar Selçuk");
        info.setEmail("serdarselcuk@gmail.com");
        info.setPhone("05325110088");
        info.setGithubUrl("https://github.com/serdardevops/DevOps-Atolyesi-Bitirme-Projesi");
        info.setDescription("All-in-One DevOps Environment with complete CI/CD Pipeline");
        info.setBuildTimestamp(Instant.now());
        info.setActiveProfile(activeProfile);
        return info;
    }
    
    public List<Map<String, String>> getDevOpsTools() {
        return Arrays.asList(
            Map.of("tool", "Jenkins", "version", "2.414.x", "purpose", "CI/CD Automation"),

            Map.of("tool", "Docker", "version", "24.0.x", "purpose", "Containerization"),
            Map.of("tool", "Kubernetes", "version", "1.32.x", "purpose", "Container Orchestration"),
            Map.of("tool", "ArgoCD", "version", "2.9.x", "purpose", "GitOps Deployment"),
            Map.of("tool", "Trivy", "version", "0.48.x", "purpose", "Security Scanning"),
            Map.of("tool", "Maven", "version", "3.9.6", "purpose", "Build Tool"),
            Map.of("tool", "Java", "version", "21", "purpose", "Runtime Environment"),
            Map.of("tool", "Spring Boot", "version", "3.2.1", "purpose", "Application Framework"),
            Map.of("tool", "Multipass", "version", "1.13.x", "purpose", "VM Management")
        );
    }
    
    public Map<String, Object> getPipelineStatus() {
        Map<String, Object> pipeline = new HashMap<>();
        pipeline.put("status", "SUCCESS");
        pipeline.put("lastBuild", Instant.now().toString());
        pipeline.put("buildNumber", "1");
        pipeline.put("duration", "5m 32s");
        
        List<Map<String, String>> stages = Arrays.asList(
            Map.of("stage", "Cleanup Workspace", "status", "SUCCESS", "duration", "5s"),
            Map.of("stage", "Checkout SCM", "status", "SUCCESS", "duration", "10s"),
            Map.of("stage", "Build Application", "status", "SUCCESS", "duration", "45s"),
            Map.of("stage", "Unit Tests", "status", "SUCCESS", "duration", "30s"),
            Map.of("stage", "Package Application", "status", "SUCCESS", "duration", "20s"),

            Map.of("stage", "Quality Gate", "status", "SUCCESS", "duration", "15s"),
            Map.of("stage", "Build Docker Image", "status", "SUCCESS", "duration", "90s"),
            Map.of("stage", "Security Scan", "status", "SUCCESS", "duration", "45s"),
            Map.of("stage", "Deploy to Kubernetes", "status", "SUCCESS", "duration", "30s"),
            Map.of("stage", "Integration Tests", "status", "SUCCESS", "duration", "25s"),
            Map.of("stage", "Update ArgoCD", "status", "SUCCESS", "duration", "10s"),
            Map.of("stage", "Cleanup Artifacts", "status", "SUCCESS", "duration", "7s")
        );
        
        pipeline.put("stages", stages);
        pipeline.put("totalStages", stages.size());
        pipeline.put("successRate", "100%");
        
        return pipeline;
    }
    
    public Map<String, String> simulateDeployment(String environment) {
        Map<String, String> deployment = new HashMap<>();
        deployment.put("status", "SUCCESS");
        deployment.put("environment", environment);
        deployment.put("deploymentId", "deploy-" + System.currentTimeMillis());
        deployment.put("timestamp", Instant.now().toString());
        deployment.put("message", "Application successfully deployed to " + environment);
        deployment.put("version", appVersion);
        deployment.put("replicas", "2");
        deployment.put("strategy", "RollingUpdate");
        return deployment;
    }
    
    public Map<String, Object> getSystemMetrics() {
        Runtime runtime = Runtime.getRuntime();
        Map<String, Object> metrics = new HashMap<>();
        
        // Memory metrics
        long maxMemory = runtime.maxMemory();
        long totalMemory = runtime.totalMemory();
        long freeMemory = runtime.freeMemory();
        long usedMemory = totalMemory - freeMemory;
        
        metrics.put("memoryUsed", formatBytes(usedMemory));
        metrics.put("memoryFree", formatBytes(freeMemory));
        metrics.put("memoryTotal", formatBytes(totalMemory));
        metrics.put("memoryMax", formatBytes(maxMemory));
        metrics.put("memoryUsagePercent", Math.round((double) usedMemory / totalMemory * 100));
        
        // System metrics
        metrics.put("availableProcessors", runtime.availableProcessors());
        metrics.put("uptime", getUptime());
        metrics.put("timestamp", Instant.now().toString());
        
        return metrics;
    }
    
    public String getActiveProfile() {
        return activeProfile;
    }
    
    private String formatBytes(long bytes) {
        if (bytes < 1024) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(1024));
        String pre = "KMGTPE".charAt(exp-1) + "";
        return String.format("%.1f %sB", bytes / Math.pow(1024, exp), pre);
    }
    
    private String getUptime() {
        long uptime = System.currentTimeMillis() - 
                     java.lang.management.ManagementFactory.getRuntimeMXBean().getStartTime();
        long seconds = uptime / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        
        return String.format("%02d:%02d:%02d", hours % 24, minutes % 60, seconds % 60);
    }
} 