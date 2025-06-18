package com.devops.atolyesi.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.Instant;

/**
 * DevOps Project Information Model
 * 
 * @author Serdar Selçuk
 */
public class DevOpsInfo {
    
    private String projectName;
    private String version;
    private String author;
    private String email;
    private String phone;
    private String githubUrl;
    private String description;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private Instant buildTimestamp;
    
    private String activeProfile;
    
    // Default constructor
    public DevOpsInfo() {}
    
    // Getters and Setters
    public String getProjectName() {
        return projectName;
    }
    
    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }
    
    public String getVersion() {
        return version;
    }
    
    public void setVersion(String version) {
        this.version = version;
    }
    
    public String getAuthor() {
        return author;
    }
    
    public void setAuthor(String author) {
        this.author = author;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public String getGithubUrl() {
        return githubUrl;
    }
    
    public void setGithubUrl(String githubUrl) {
        this.githubUrl = githubUrl;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public Instant getBuildTimestamp() {
        return buildTimestamp;
    }
    
    public void setBuildTimestamp(Instant buildTimestamp) {
        this.buildTimestamp = buildTimestamp;
    }
    
    public String getActiveProfile() {
        return activeProfile;
    }
    
    public void setActiveProfile(String activeProfile) {
        this.activeProfile = activeProfile;
    }
    
    @Override
    public String toString() {
        return "DevOpsInfo{" +
                "projectName='" + projectName + '\'' +
                ", version='" + version + '\'' +
                ", author='" + author + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", githubUrl='" + githubUrl + '\'' +
                ", description='" + description + '\'' +
                ", buildTimestamp=" + buildTimestamp +
                ", activeProfile='" + activeProfile + '\'' +
                '}';
    }
} 