package com.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

//Try updating your CORS config to be more explicit

import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//CorsConfig.java - Replace with this
@Configuration
public class CorsConfig implements WebMvcConfigurer {
 @Override
 public void addCorsMappings(CorsRegistry registry) {
     registry.addMapping("/**")
             .allowedOrigins("http://localhost:8083")
             .allowedMethods("*")
             .allowedHeaders("*")
             .exposedHeaders("*")
             .allowCredentials(false)
             .maxAge(3600);
 }
}