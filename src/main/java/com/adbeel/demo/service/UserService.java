/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */

package com.adbeel.demo.service;

/**
 *
 * @author Laboratorio
 */
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.adbeel.demo.domain.User;
import com.adbeel.demo.service.dto.UserDTO;
import com.adbeel.demo.service.dto.UserResponseDTO;

public interface UserService {
    
    // CRUD básico
    List<User> findAll();
    Optional<User> findById(Long id);
    User save(User user);
    User saveFromDTO(UserDTO userDTO);
    void deleteById(Long id);
    
    // Métodos específicos
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    
    // Consultas avanzadas (requeridas en el ejercicio)
    List<User> findByRoleName(String roleName);
    List<User> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
    List<User> findByEmailContainingOrNameContaining(String email, String name);
    List<User> findAllByOrderByCreatedAtDesc();
    
    // Estadísticas
    long countByActive(boolean active);
    Map<String, Long> countUsersByRole();
    
    // Métodos de negocio
    User registerUser(UserDTO userDTO);
    User updateUser(Long id, UserDTO userDTO);
    void deactivateUser(Long id);
    void activateUser(Long id);
    
    // Para Thymeleaf
    Page<UserResponseDTO> findAllPaginated(Pageable pageable);
    List<UserResponseDTO> convertToResponseDTO(List<User> users);
    UserResponseDTO convertToResponseDTO(User user);
    
    // Búsqueda avanzada
    List<User> searchUsers(String keyword, String role, Boolean active);
}
