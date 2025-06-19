package com.devops.atolyesi.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Home Controller for serving static HTML pages
 * 
 * @author Serdar Selçuk
 */
@Controller
public class HomeController {
    
    @GetMapping("/")
    public String home() {
        return "forward:/index.html";
    }
    
    @GetMapping("/dashboard")
    public String dashboard() {
        return "forward:/index.html";
    }
} 