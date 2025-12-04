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
import com.adbeel.demo.domain.User;
import com.adbeel.demo.service.RoleService;
import com.adbeel.demo.service.UserService;
import com.adbeel.demo.service.dto.UserDTO;
import com.adbeel.demo.service.dto.UserResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin/users")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class UserController {
    
    private final UserService userService;
    private final RoleService roleService;
    private final SecurityUtils securityUtils;
    
    @GetMapping
    public String listUsers(@RequestParam(defaultValue = "0") int page,
                           @RequestParam(defaultValue = "10") int size,
                           @RequestParam(required = false) String search,
                           Model model) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<UserResponseDTO> userPage;
        
        if (search != null && !search.trim().isEmpty()) {
            // Buscar usuarios
            List<User> users = userService.searchUsers(search, null, null);
            List<UserResponseDTO> userDTOs = userService.convertToResponseDTO(users);
            userPage = new org.springframework.data.domain.PageImpl<>(
                userDTOs, pageable, users.size()
            );
            model.addAttribute("search", search);
        } else {
            userPage = userService.findAllPaginated(pageable);
        }
        
        model.addAttribute("usersPage", userPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", userPage.getTotalPages());
        model.addAttribute("totalItems", userPage.getTotalElements());
        model.addAttribute("pageTitle", "Gestión de Usuarios");
        
        return "admin/users/list";
    }
    
    @GetMapping("/create")
    public String showCreateForm(Model model) {
        UserDTO userDTO = new UserDTO();
        userDTO.setActive(true);
        
        model.addAttribute("userDTO", userDTO);
        model.addAttribute("roles", roleService.findAllSortedByName());
        model.addAttribute("pageTitle", "Crear Nuevo Usuario");
        
        return "admin/users/create";
    }
    
    @PostMapping("/create")
    public String createUser(@Valid @ModelAttribute UserDTO userDTO,
                            BindingResult result,
                            Model model,
                            RedirectAttributes redirectAttributes) {
        
        if (result.hasErrors()) {
            model.addAttribute("roles", roleService.findAllSortedByName());
            model.addAttribute("pageTitle", "Crear Nuevo Usuario");
            return "admin/users/create";
        }
        
        try {
            userService.saveFromDTO(userDTO);
            redirectAttributes.addFlashAttribute("successMessage", 
                "Usuario creado exitosamente");
            return "redirect:/admin/users";
            
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("roles", roleService.findAllSortedByName());
            model.addAttribute("pageTitle", "Crear Nuevo Usuario");
            return "admin/users/create";
        }
    }
    
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Optional<User> userOptional = userService.findById(id);
        
        if (userOptional.isEmpty()) {
            return "redirect:/admin/users";
        }
        
        User user = userOptional.get();
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setName(user.getName());
        userDTO.setEmail(user.getEmail());
        userDTO.setActive(user.isActive());
        userDTO.setRoleId(user.getRole().getId());
        
        model.addAttribute("userDTO", userDTO);
        model.addAttribute("roles", roleService.findAllSortedByName());
        model.addAttribute("pageTitle", "Editar Usuario");
        
        return "admin/users/edit";
    }
    
    @PostMapping("/edit/{id}")
    public String updateUser(@PathVariable Long id,
                            @Valid @ModelAttribute UserDTO userDTO,
                            BindingResult result,
                            Model model,
                            RedirectAttributes redirectAttributes) {
        
        if (result.hasErrors()) {
            model.addAttribute("roles", roleService.findAllSortedByName());
            model.addAttribute("pageTitle", "Editar Usuario");
            return "admin/users/edit";
        }
        
        try {
            userService.updateUser(id, userDTO);
            redirectAttributes.addFlashAttribute("successMessage", 
                "Usuario actualizado exitosamente");
            return "redirect:/admin/users";
            
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("roles", roleService.findAllSortedByName());
            model.addAttribute("pageTitle", "Editar Usuario");
            return "admin/users/edit";
        }
    }
    
    @GetMapping("/detail/{id}")
    public String showUserDetail(@PathVariable Long id, Model model) {
        Optional<User> userOptional = userService.findById(id);
        
        if (userOptional.isEmpty()) {
            return "redirect:/admin/users";
        }
        
        UserResponseDTO userDTO = userService.convertToResponseDTO(userOptional.get());
        
        model.addAttribute("user", userDTO);
        model.addAttribute("pageTitle", "Detalle de Usuario");
        
        return "admin/users/detail";
    }
    
    @PostMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id,
                            RedirectAttributes redirectAttributes) {
        
        // No permitir eliminar al propio usuario
        securityUtils.getCurrentUsername().ifPresent(currentEmail -> {
            userService.findById(id).ifPresent(user -> {
                if (user.getEmail().equals(currentEmail)) {
                    throw new IllegalStateException("No puedes eliminar tu propio usuario");
                }
            });
        });
        
        try {
            userService.deleteById(id);
            redirectAttributes.addFlashAttribute("successMessage", 
                "Usuario eliminado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", 
                "Error al eliminar el usuario: " + e.getMessage());
        }
        
        return "redirect:/admin/users";
    }
    
    @PostMapping("/toggle-status/{id}")
    public String toggleUserStatus(@PathVariable Long id,
                                  @RequestParam boolean activate,
                                  RedirectAttributes redirectAttributes) {
        
        try {
            if (activate) {
                userService.activateUser(id);
                redirectAttributes.addFlashAttribute("successMessage", 
                    "Usuario activado exitosamente");
            } else {
                userService.deactivateUser(id);
                redirectAttributes.addFlashAttribute("successMessage", 
                    "Usuario desactivado exitosamente");
            }
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        
        return "redirect:/admin/users";
    }
}
