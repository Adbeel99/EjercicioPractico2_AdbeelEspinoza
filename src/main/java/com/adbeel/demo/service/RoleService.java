/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */

package com.adbeel.demo.service;

/**
 *
 * @author Laboratorio
 */
import com.adbeel.demo.domain.Role;
import java.util.List;
import java.util.Optional;

public interface RoleService {
    
    // CRUD básico
    List<Role> findAll();
    Optional<Role> findById(Long id);
    Role save(Role role);
    void deleteById(Long id);
    
    // Métodos específicos
    Optional<Role> findByName(String name);
    boolean existsByName(String name);
    
    // Métodos para validación
    boolean canDeleteRole(Long roleId);
    List<Role> findAllSortedByName();
    
    // Métodos para el ejercicio
    Role createDefaultRolesIfNotExist();
}