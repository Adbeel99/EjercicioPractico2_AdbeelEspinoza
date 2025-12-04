/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.adbeel.demo.controllers;

/**
 *
 * @author Laboratorio
 */
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.adbeel.demo.service.UserService;
import com.adbeel.demo.service.dto.UserDTO;

import jakarta.validation.Valid;

@Controller
public class AuthController {
    
    @Autowired
    private UserService userService;
    
    @GetMapping("/login")
    public String loginPage(@RequestParam(value = "error", required = false) String error,
                           @RequestParam(value = "logout", required = false) String logout,
                           @RequestParam(value = "registered", required = false) String registered,
                           Model model) {
        
        if (error != null) {
            model.addAttribute("errorMessage", "Email o contraseña incorrectos");
        }
        
        if (logout != null) {
            model.addAttribute("successMessage", "Has cerrado sesión correctamente");
        }
        
        if (registered != null) {
            model.addAttribute("successMessage", "¡Registro exitoso! Por favor inicia sesión");
        }
        
        model.addAttribute("pageTitle", "Iniciar Sesión");
        return "auth/login";
    }
    
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("userDTO", new UserDTO());
        model.addAttribute("pageTitle", "Registro de Usuario");
        return "auth/register";
    }
    
    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute UserDTO userDTO,
                              BindingResult result,
                              Model model,
                              RedirectAttributes redirectAttributes) {
        
        if (result.hasErrors()) {
            model.addAttribute("pageTitle", "Registro de Usuario");
            return "auth/register";
        }
        
        try {
            // Por defecto, los usuarios registrados son ESTUDIANTES
            // Si se necesita otro rol, se debe asignar desde la gestión de usuarios
            userService.registerUser(userDTO);
            
            redirectAttributes.addFlashAttribute("successMessage", 
                "Usuario registrado exitosamente. Por favor inicia sesión.");
            return "redirect:/login?registered=true";
            
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("pageTitle", "Registro de Usuario");
            return "auth/register";
        }
    }
    
    @GetMapping("/access-denied")
    public String accessDenied(Model model) {
        model.addAttribute("pageTitle", "Acceso Denegado");
        return "error/access-denied";
    }
}
