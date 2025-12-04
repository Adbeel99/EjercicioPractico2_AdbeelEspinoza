/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.adbeel.demo.service;

/**
 *
 * @author Laboratorio
 */
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.adbeel.demo.domain.Role;
import com.adbeel.demo.domain.User;
import com.adbeel.demo.repository.RoleRepository;
import com.adbeel.demo.repository.UserRepository;

@Service
@Transactional
public class RoleServiceImpl implements RoleService {
    
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;
    
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
        if (role.getId() == null && roleRepository.existsByNombre(role.getNombre())) {
            throw new IllegalArgumentException("Ya existe un rol con el nombre: " + role.getNombre());
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
        return roleRepository.findByNombre(name);
    }
    
    @Override
    public boolean existsByName(String name) {
        return roleRepository.existsByNombre(name);
    }
    
    @Override
    public boolean canDeleteRole(Long roleId) {
        // Verificar si hay usuarios con este rol
        List<User> usersWithRole = userRepository.findByRolNombre(
            roleRepository.findById(roleId)
                .map(Role::getNombre)
                .orElse("")
        );
        return usersWithRole.isEmpty();
    }
    
    @Override
    public List<Role> findAllSortedByName() {
        return roleRepository.findAll().stream()
            .sorted((r1, r2) -> r1.getNombre().compareToIgnoreCase(r2.getNombre()))
            .toList();
    }
    
    @Override
    public Role createDefaultRolesIfNotExist() {
        List<String> defaultRoles = Arrays.asList("ADMIN", "PROFESOR", "ESTUDIANTE");
        
        for (String roleName : defaultRoles) {
            if (!roleRepository.existsByNombre(roleName)) {
                Role role = new Role();
                role.setNombre(roleName);
                roleRepository.save(role);
            }
        }

        return roleRepository.findByNombre("ADMIN")
            .orElseThrow(() -> new RuntimeException("Error creando roles por defecto"));
    }
}
