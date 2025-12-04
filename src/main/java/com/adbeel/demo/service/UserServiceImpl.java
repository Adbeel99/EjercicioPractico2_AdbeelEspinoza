/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.adbeel.demo.service;

/**
 *
 * @author Laboratorio
 */
import com.adbeel.demo.domain.Role;
import com.adbeel.demo.domain.User;
import com.adbeel.demo.repository.RoleRepository;
import com.adbeel.demo.repository.UserRepository;
import com.adbeel.demo.service.RoleService;
import com.adbeel.demo.service.UserService;
import com.adbeel.demo.service.dto.UserDTO;
import com.adbeel.demo.service.dto.UserResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {
    
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;
    
    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }
    
    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }
    
    @Override
    public User save(User user) {
        if (user.getId() == null && userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Ya existe un usuario con el email: " + user.getEmail());
        }
        
        // Si es un usuario nuevo, encriptar la contraseña
        if (user.getId() == null && user.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        
        return userRepository.save(user);
    }
    
    @Override
    public User saveFromDTO(UserDTO userDTO) {
        User user = convertToEntity(userDTO);
        return save(user);
    }
    
    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }
    
    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    
    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
    
    // CONSULTAS AVANZADAS (requeridas en el ejercicio)
    
    @Override
    public List<User> findByRoleName(String roleName) {
        return userRepository.findByRoleName(roleName);
    }
    
    @Override
    public List<User> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end) {
        return userRepository.findByCreatedAtBetween(start, end);
    }
    
    @Override
    public List<User> findByEmailContainingOrNameContaining(String email, String name) {
        return userRepository.findByEmailContainingOrNameContaining(email, name);
    }
    
    @Override
    public List<User> findAllByOrderByCreatedAtDesc() {
        return userRepository.findAllByOrderByCreatedAtDesc();
    }
    
    @Override
    public long countByActive(boolean active) {
        return userRepository.countByActiveStatus(active);
    }
    
    @Override
    public Map<String, Long> countUsersByRole() {
        List<Object[]> results = userRepository.countUsersByRole();
        return results.stream()
            .collect(Collectors.toMap(
                result -> (String) result[0],
                result -> (Long) result[1]
            ));
    }
    
    // MÉTODOS DE NEGOCIO
    
    @Override
    public User registerUser(UserDTO userDTO) {
        // Verificar que el email no exista
        if (existsByEmail(userDTO.getEmail())) {
            throw new IllegalArgumentException("El email ya está registrado");
        }
        
        return saveFromDTO(userDTO);
    }
    
    @Override
    public User updateUser(Long id, UserDTO userDTO) {
        User existingUser = userRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        
        // Verificar si el email cambió y si ya existe
        if (!existingUser.getEmail().equals(userDTO.getEmail()) && 
            existsByEmail(userDTO.getEmail())) {
            throw new IllegalArgumentException("El email ya está registrado");
        }
        
        existingUser.setName(userDTO.getName());
        existingUser.setEmail(userDTO.getEmail());
        existingUser.setActive(userDTO.isActive());
        
        // Actualizar contraseña solo si se proporciona una nueva
        if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        }
        
        // Actualizar rol si es necesario
        if (userDTO.getRoleId() != null && 
            !existingUser.getRole().getId().equals(userDTO.getRoleId())) {
            Role newRole = roleRepository.findById(userDTO.getRoleId())
                .orElseThrow(() -> new IllegalArgumentException("Rol no encontrado"));
            existingUser.setRole(newRole);
        }
        
        return userRepository.save(existingUser);
    }
    
    @Override
    public void deactivateUser(Long id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        user.setActive(false);
        userRepository.save(user);
    }
    
    @Override
    public void activateUser(Long id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        user.setActive(true);
        userRepository.save(user);
    }
    
    // MÉTODOS PARA THYMELEAF
    
    @Override
    public Page<UserResponseDTO> findAllPaginated(Pageable pageable) {
        Page<User> userPage = userRepository.findAll(pageable);
        List<UserResponseDTO> dtoList = convertToResponseDTO(userPage.getContent());
        return new PageImpl<>(dtoList, pageable, userPage.getTotalElements());
    }
    
    @Override
    public List<UserResponseDTO> convertToResponseDTO(List<User> users) {
        return users.stream()
            .map(this::convertToResponseDTO)
            .collect(Collectors.toList());
    }
    
    @Override
    public UserResponseDTO convertToResponseDTO(User user) {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setActive(user.isActive());
        dto.setCreatedAt(user.getCreatedAt());
        dto.setRoleName(user.getRole().getName());
        dto.setRoleId(user.getRole().getId());
        return dto;
    }
    
    @Override
    public List<User> searchUsers(String keyword, String role, Boolean active) {
        // Implementación simplificada de búsqueda
        if (keyword != null && !keyword.trim().isEmpty()) {
            return userRepository.findByEmailContainingOrNameContaining(keyword, keyword);
        }
        
        if (role != null && !role.trim().isEmpty()) {
            return userRepository.findByRoleName(role);
        }
        
        if (active != null) {
            return userRepository.findByActive(active);
        }
        
        return userRepository.findAll();
    }
    
    // MÉTODOS PRIVADOS DE CONVERSIÓN
    
    private User convertToEntity(UserDTO userDTO) {
        User user = new User();
        user.setId(userDTO.getId());
        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        
        // Encriptar contraseña
        if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        }
        
        user.setActive(userDTO.isActive());
        
        // Asignar rol
        Role role = roleRepository.findById(userDTO.getRoleId())
            .orElseThrow(() -> new IllegalArgumentException("Rol no encontrado"));
        user.setRole(role);
        
        return user;
    }
    }