/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */

package com.adbeel.demo.repository;

/**
 *
 * @author Laboratorio
 */
import com.adbeel.demo.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    
    // Buscar rol por nombre
    Optional<Role> findByName(String name);
    
    // Verificar si existe un rol por nombre
    boolean existsByName(String name);
    
    // Eliminar rol por nombre
    void deleteByName(String name);
}
