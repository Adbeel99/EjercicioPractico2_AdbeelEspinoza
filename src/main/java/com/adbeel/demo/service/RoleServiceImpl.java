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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class RoleServiceImpl implements RoleService {
    
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    
    @Override
    public List<Role> findAll() {
        return roleRepository.findAll();
    }
    
    @Override
    public Optional<Role> findById(Long id) {
        return roleRepository.findById(id);
    }
    
    @Override
    public Role save(Role role) {
        // Validar que el nombre no exista (excepto en actualización)
        if (role.getId() == null && roleRepository.existsByName(role.getName())) {
            throw new IllegalArgumentException("Ya existe un rol con el nombre: " + role.getName());
        }
        return roleRepository.save(role);
    }
    
    @Override
    public void deleteById(Long id) {
        if (!canDeleteRole(id)) {
            throw new IllegalStateException("No se puede eliminar el rol porque tiene usuarios asignados");
        }
        roleRepository.deleteById(id);
    }
    
    @Override
    public Optional<Role> findByName(String name) {
        return roleRepository.findByName(name);
    }
    
    @Override
    public boolean existsByName(String name) {
        return roleRepository.existsByName(name);
    }
    
    @Override
    public boolean canDeleteRole(Long roleId) {
        // Verificar si hay usuarios con este rol
        List<User> usersWithRole = userRepository.findByRoleName(
            roleRepository.findById(roleId)
                .map(Role::getName)
                .orElse("")
        );
        return usersWithRole.isEmpty();
    }
    
    @Override
    public List<Role> findAllSortedByName() {
        return roleRepository.findAll().stream()
            .sorted((r1, r2) -> r1.getName().compareToIgnoreCase(r2.getName()))
            .toList();
    }
    
    @Override
    public Role createDefaultRolesIfNotExist() {
        List<String> defaultRoles = Arrays.asList("ADMIN", "PROFESOR", "ESTUDIANTE");
        
        for (String roleName : defaultRoles) {
            if (!roleRepository.existsByName(roleName)) {
                Role role = new Role();
                role.setName(roleName);
                roleRepository.save(role);
            }
        }
        
        return roleRepository.findByName("ADMIN")
            .orElseThrow(() -> new RuntimeException("Error creando roles por defecto"));
    }
}
