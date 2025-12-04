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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class HomeController {
    
    private final SecurityUtils securityUtils;
    
    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("pageTitle", "Plataforma Académica - Inicio");
        
        // Añadir información del usuario si está logueado
        securityUtils.getCurrentUsername().ifPresent(username -> {
            model.addAttribute("currentUser", username);
            model.addAttribute("currentRole", securityUtils.getCurrentUserRole());
        });
        
        return "home";
    }
    
    @GetMapping("/dashboard")
    public String dashboard() {
        // Redirigir al dashboard según el rol
        if (securityUtils.isAdmin()) {
            return "redirect:/admin/dashboard";
        } else if (securityUtils.isProfesor()) {
            return "redirect:/profesor/dashboard";
        } else if (securityUtils.isEstudiante()) {
            return "redirect:/estudiante/dashboard";
        }
        return "redirect:/";
    }
}
