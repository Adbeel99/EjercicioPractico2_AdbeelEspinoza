/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */

package com.adbeel.demo.repository;

/**
 *
 * @author Laboratorio
 */
import com.adbeel.demo.domain.User;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserCustomRepository {
    
    // Método para búsqueda avanzada con paginación
    Page<User> searchUsers(String keyword, String role, Boolean active, 
                          LocalDateTime startDate, LocalDateTime endDate, 
                          Pageable pageable);
    
    // Obtener usuarios con sus detalles completos (DTO projection)
    List<UserProjection> findAllWithDetails();
    
    // Interfaz para proyección
    interface UserProjection {
        Long getId();
        String getName();
        String getEmail();
        String getRoleName();
        LocalDateTime getCreatedAt();
        boolean isActive();
    }
}
