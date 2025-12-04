/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.adbeel.demo.controllers;

/**
 *
 * @author Laboratorio
 */
import com.adbeel.demo.config.SecurityUtils;
import com.adbeel.demo.service.AdvancedQueriesService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
@RequestMapping("/profesor")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('PROFESOR', 'ADMIN')")
public class ProfesorController {
    
    private final AdvancedQueriesService advancedQueriesService;
    private final SecurityUtils securityUtils;
    
    @GetMapping("/dashboard")
    public String profesorDashboard(Model model) {
        model.addAttribute("pageTitle", "Dashboard del Profesor");
        return "profesor/dashboard";
    }
    
    @GetMapping("/reportes")
    public String showReports(Model model) {
        // Últimos 10 usuarios registrados
        List<?> latestUsers = advancedQueriesService.getLatestUsers(10);
        
        // Usuarios activos
        List<?> activeUsers = advancedQueriesService.getActiveUsers();
        
        // Estadísticas básicas
        var stats = advancedQueriesService.getUserStatistics();
        
        model.addAttribute("latestUsers", latestUsers);
        model.addAttribute("activeUsers", activeUsers);
        model.addAttribute("stats", stats);
        model.addAttribute("pageTitle", "Reportes del Sistema");
        
        return "profesor/reportes";
    }
    
    @GetMapping("/estudiantes")
    public String listEstudiantes(Model model) {
        List<?> estudiantes = advancedQueriesService.getUsersByRole("ESTUDIANTE");
        
        model.addAttribute("estudiantes", estudiantes);
        model.addAttribute("pageTitle", "Lista de Estudiantes");
        
        return "profesor/estudiantes";
    }
}
