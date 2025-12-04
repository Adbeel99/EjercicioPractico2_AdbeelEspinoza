/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.adbeel.demo.controllers;

/**
 *
 * @author Laboratorio
 */
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.adbeel.demo.domain.Role;
import com.adbeel.demo.service.RoleService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/admin/roles")
@PreAuthorize("hasRole('ADMIN')")
public class RoleController {
    
    @Autowired
    private RoleService roleService;
    
    @GetMapping
    public String listRoles(Model model) {
        List<Role> roles = roleService.findAllSortedByName();
        
        model.addAttribute("roles", roles);
        model.addAttribute("pageTitle", "Gestión de Roles");
        
        return "admin/roles/list";
    }
    
    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("role", new Role());
        model.addAttribute("pageTitle", "Crear Nuevo Rol");
        return "admin/roles/create";
    }
    
    @PostMapping("/create")
    public String createRole(@Valid @ModelAttribute Role role,
                            BindingResult result,
                            Model model,
                            RedirectAttributes redirectAttributes) {
        
        if (result.hasErrors()) {
            model.addAttribute("pageTitle", "Crear Nuevo Rol");
            return "admin/roles/create";
        }
        
        try {
            roleService.save(role);
            redirectAttributes.addFlashAttribute("successMessage", 
                "Rol creado exitosamente");
            return "redirect:/admin/roles";
            
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("pageTitle", "Crear Nuevo Rol");
            return "admin/roles/create";
        }
    }
    
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Optional<Role> roleOptional = roleService.findById(id);
        
        if (roleOptional.isEmpty()) {
            return "redirect:/admin/roles";
        }
        
        model.addAttribute("role", roleOptional.get());
        model.addAttribute("pageTitle", "Editar Rol");
        
        return "admin/roles/edit";
    }
    
    @PostMapping("/edit/{id}")
    public String updateRole(@PathVariable Long id,
                            @Valid @ModelAttribute Role role,
                            BindingResult result,
                            Model model,
                            RedirectAttributes redirectAttributes) {
        
        if (result.hasErrors()) {
            model.addAttribute("pageTitle", "Editar Rol");
            return "admin/roles/edit";
        }
        
        try {
            role.setId(id); // Asegurar que el ID sea el correcto
            roleService.save(role);
            redirectAttributes.addFlashAttribute("successMessage", 
                "Rol actualizado exitosamente");
            return "redirect:/admin/roles";
            
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("pageTitle", "Editar Rol");
            return "admin/roles/edit";
        }
    }
    
    @GetMapping("/detail/{id}")
    public String showRoleDetail(@PathVariable Long id, Model model) {
        Optional<Role> roleOptional = roleService.findById(id);
        
        if (roleOptional.isEmpty()) {
            return "redirect:/admin/roles";
        }
        
        Role role = roleOptional.get();
        boolean canDelete = roleService.canDeleteRole(id);
        
        model.addAttribute("role", role);
        model.addAttribute("canDelete", canDelete);
        model.addAttribute("pageTitle", "Detalle del Rol: " + role.getName());
        
        return "admin/roles/detail";
    }
    
    @PostMapping("/delete/{id}")
    public String deleteRole(@PathVariable Long id,
                            RedirectAttributes redirectAttributes) {
        
        try {
            if (!roleService.canDeleteRole(id)) {
                redirectAttributes.addFlashAttribute("errorMessage", 
                    "No se puede eliminar el rol porque tiene usuarios asignados");
                return "redirect:/admin/roles/detail/" + id;
            }
            
            roleService.deleteById(id);
            redirectAttributes.addFlashAttribute("successMessage", 
                "Rol eliminado exitosamente");
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", 
                "Error al eliminar el rol: " + e.getMessage());
        }
        
        return "redirect:/admin/roles";
    }
}
