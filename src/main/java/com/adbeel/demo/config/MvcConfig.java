/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.adbeel.demo.config;

/**
 *
 * @author Laboratorio
 */
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {
    
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // Redirecciones para páginas estáticas
        registry.addViewController("/").setViewName("home");
        registry.addViewController("/login").setViewName("auth/login");
        registry.addViewController("/access-denied").setViewName("error/access-denied");
        registry.addViewController("/admin/dashboard").setViewName("admin/dashboard");
        registry.addViewController("/profesor/dashboard").setViewName("profesor/dashboard");
        registry.addViewController("/estudiante/dashboard").setViewName("estudiante/dashboard");
    }
    
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(
            "/css/**",
            "/js/**",
            "/images/**",
            "/webjars/**"
        ).addResourceLocations(
            "classpath:/static/css/",
            "classpath:/static/js/",
            "classpath:/static/images/",
            "/webjars/"
        );
    }
}