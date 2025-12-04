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
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    // 1. Buscar usuarios por rol (derived query)
    List<User> findByRoleName(String roleName);
    
    // 2. Buscar usuarios por coincidencia parcial en correo o nombre
    List<User> findByEmailContainingOrNameContaining(String email, String name);
    
    // 3. Buscar usuarios creados en un rango de fechas
    List<User> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    // 4. Buscar usuarios activos/inactivos
    List<User> findByActive(boolean active);
    
    // 5. Obtener usuarios ordenados por fecha de creación (más recientes primero)
    List<User> findAllByOrderByCreatedAtDesc();
    
    // 6. Buscar por email exacto
    Optional<User> findByEmail(String email);
    
    // 7. Verificar si existe un usuario por email
    boolean existsByEmail(String email);
    
    // CONSULTAS PERSONALIZADAS CON @Query
    
    // 8. Contar usuarios activos vs inactivos (una de las requeridas)
    @Query("SELECT COUNT(u) FROM User u WHERE u.active = :active")
    long countByActiveStatus(@Param("active") boolean active);
    
    // 9. Buscar usuarios por dominio de email
    @Query("SELECT u FROM User u WHERE u.email LIKE %:domain")
    List<User> findByEmailDomain(@Param("domain") String domain);
    
    // 10. Obtener estadísticas de usuarios por rol
    @Query("SELECT r.name, COUNT(u) FROM User u JOIN u.role r GROUP BY r.name")
    List<Object[]> countUsersByRole();
    
    // 11. Buscar usuarios con rol específico y activos
    @Query("SELECT u FROM User u WHERE u.role.name = :roleName AND u.active = :active")
    List<User> findByRoleNameAndActive(@Param("roleName") String roleName, @Param("active") boolean active);
    
    // 12. Obtener últimos N usuarios registrados
    @Query(value = "SELECT * FROM users ORDER BY created_at DESC LIMIT :limit", nativeQuery = true)
    List<User> findLatestUsers(@Param("limit") int limit);
    
    // 13. Buscar usuarios con nombre que empiece con...
    List<User> findByNameStartingWith(String prefix);
    
    // 14. Buscar usuarios con nombre que termine con...
    List<User> findByNameEndingWith(String suffix);
    
    // 15. Eliminar usuarios inactivos por más de X días
    @Query("DELETE FROM User u WHERE u.active = false AND u.createdAt < :date")
    int deleteInactiveUsersBefore(@Param("date") LocalDateTime date);
}