/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.adbeel.demo.controllers;

/**
 *
 * @author Laboratorio
 */
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.adbeel.demo.config.SecurityUtils;
import com.adbeel.demo.domain.User;
import com.adbeel.demo.service.UserService;

@Controller
@RequestMapping("/estudiante")
@PreAuthorize("hasAnyRole('ESTUDIANTE', 'PROFESOR', 'ADMIN')")
public class EstudianteController {
    
    @Autowired
    private UserService userService;
    @Autowired
    private SecurityUtils securityUtils;
    
    @GetMapping("/dashboard")
    public String estudianteDashboard(Model model) {
        model.addAttribute("pageTitle", "Dashboard del Estudiante");
        return "estudiante/dashboard";
    }
    
    @GetMapping("/perfil")
    public String showProfile(Model model) {
        // Obtener el usuario actual
        Optional<String> currentEmail = securityUtils.getCurrentUsername();
        
        if (currentEmail.isPresent()) {
            Optional<User> userOptional = userService.findByEmail(currentEmail.get());
            
            if (userOptional.isPresent()) {
                var userDTO = userService.convertToResponseDTO(userOptional.get());
                model.addAttribute("user", userDTO);
            }
        }
        
        model.addAttribute("pageTitle", "Mi Perfil");
        return "estudiante/perfil";
    }
    
    @GetMapping("/mis-datos")
    public String showMyData(Model model) {
        // Similar al perfil pero con opción de edición
        Optional<String> currentEmail = securityUtils.getCurrentUsername();
        
        if (currentEmail.isPresent()) {
            Optional<User> userOptional = userService.findByEmail(currentEmail.get());
            
            if (userOptional.isPresent()) {
                var userDTO = userService.convertToResponseDTO(userOptional.get());
                model.addAttribute("user", userDTO);
            }
        }
        
        model.addAttribute("pageTitle", "Mis Datos");
        return "estudiante/mis-datos";
    }
}
