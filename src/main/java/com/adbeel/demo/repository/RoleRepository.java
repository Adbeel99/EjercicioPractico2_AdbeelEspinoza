/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */

package com.adbeel.demo.repository;

/**
 *
 * @author Laboratorio
 */
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.adbeel.demo.domain.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    
    // Buscar rol por nombre (propiedad en español)
    Optional<Role> findByNombre(String nombre);

    // Verificar si existe un rol por nombre
    boolean existsByNombre(String nombre);

    // Eliminar rol por nombre
    void deleteByNombre(String nombre);
}
