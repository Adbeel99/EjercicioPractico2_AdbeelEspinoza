/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.adbeel.demo.controllers;

/**
 *
 * @author Laboratorio
 */
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.adbeel.demo.service.AdvancedQueriesService;

@Controller
@RequestMapping("/consultas")
@PreAuthorize("hasAnyRole('PROFESOR', 'ADMIN')")
public class AdvancedQueriesController {
    
    @Autowired
    private AdvancedQueriesService advancedQueriesService;
    
    @GetMapping("/avanzadas")
    public String showAdvancedQueriesPage(Model model) {
        model.addAttribute("pageTitle", "Consultas Avanzadas");
        return "consultas/avanzadas";
    }
    
    @GetMapping("/por-rol")
    public String queryByRole(@RequestParam String rol, Model model) {
        List<?> users = advancedQueriesService.getUsersByRole(rol);
        
        model.addAttribute("users", users);
        model.addAttribute("rol", rol);
        model.addAttribute("pageTitle", "Usuarios por Rol: " + rol);
        model.addAttribute("queryType", "por-rol");
        
        return "consultas/resultados";
    }
    
    @GetMapping("/por-fecha")
    public String queryByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin,
            Model model) {
        
        List<?> users = advancedQueriesService.getUsersByDateRange(fechaInicio, fechaFin);
        
        model.addAttribute("users", users);
        model.addAttribute("fechaInicio", fechaInicio);
        model.addAttribute("fechaFin", fechaFin);
        model.addAttribute("pageTitle", "Usuarios por Rango de Fechas");
        model.addAttribute("queryType", "por-fecha");
        
        return "consultas/resultados";
    }
    
    @GetMapping("/por-busqueda")
    public String queryBySearch(@RequestParam String termino, Model model) {
        List<?> users = advancedQueriesService.getUsersByPartialMatch(termino);
        
        model.addAttribute("users", users);
        model.addAttribute("termino", termino);
        model.addAttribute("pageTitle", "Resultados de búsqueda: " + termino);
        model.addAttribute("queryType", "por-busqueda");
        
        return "consultas/resultados";
    }
    
    @GetMapping("/ordenados-fecha")
    public String queryOrderedByDate(Model model) {
        List<?> users = advancedQueriesService.getUsersOrderedByCreationDate();
        
        model.addAttribute("users", users);
        model.addAttribute("pageTitle", "Usuarios Ordenados por Fecha de Creación");
        model.addAttribute("queryType", "ordenados-fecha");
        
        return "consultas/resultados";
    }
    
    @GetMapping("/estadisticas")
    public String showStatistics(Model model) {
        var stats = advancedQueriesService.getUserStatistics();
        
        model.addAttribute("stats", stats);
        model.addAttribute("pageTitle", "Estadísticas del Sistema");
        model.addAttribute("queryType", "estadisticas");
        
        return "consultas/resultados";
    }
    
    @GetMapping("/activos-inactivos")
    public String queryActiveInactive(Model model) {
        List<?> activeUsers = advancedQueriesService.getActiveUsers();
        List<?> inactiveUsers = advancedQueriesService.getInactiveUsers();
        
        model.addAttribute("activeUsers", activeUsers);
        model.addAttribute("inactiveUsers", inactiveUsers);
        model.addAttribute("pageTitle", "Usuarios Activos vs Inactivos");
        model.addAttribute("queryType", "activos-inactivos");
        
        return "consultas/resultados";
    }
}
