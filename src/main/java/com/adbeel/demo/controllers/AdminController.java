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
import com.adbeel.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
    
    private final UserService userService;
    private final AdvancedQueriesService advancedQueriesService;
    private final SecurityUtils securityUtils;
    
    @GetMapping("/dashboard")
    public String adminDashboard(Model model) {
        // Estadísticas para el dashboard
        long totalUsers = userService.findAll().size();
        long activeUsers = userService.countByActive(true);
        long inactiveUsers = userService.countByActive(false);
        Map<String, Long> usersByRole = userService.countUsersByRole();
        
        model.addAttribute("totalUsers", totalUsers);
        model.addAttribute("activeUsers", activeUsers);
        model.addAttribute("inactiveUsers", inactiveUsers);
        model.addAttribute("usersByRole", usersByRole);
        model.addAttribute("pageTitle", "Dashboard de Administrador");
        
        return "admin/dashboard";
    }
    
    @GetMapping("/stats")
    public String showStatistics(Model model) {
        Map<String, Object> stats = advancedQueriesService.getUserStatistics();
        model.addAttribute("stats", stats);
        model.addAttribute("pageTitle", "Estadísticas del Sistema");
        return "admin/stats";
    }
}
