/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */

package com.adbeel.demo.repository;

/**
 *
 * @author Laboratorio
 */
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.adbeel.demo.domain.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    // 1. Buscar usuarios por rol (derived query) — usa propiedades en español
    List<User> findByRolNombre(String nombre);

    // 2. Buscar usuarios por coincidencia parcial en correo o nombre
    List<User> findByEmailContainingOrNombreContaining(String email, String nombre);

    // 3. Buscar usuarios creados en un rango de fechas
    List<User> findByFechaCreacionBetween(LocalDateTime startDate, LocalDateTime endDate);

    // 4. Buscar usuarios activos/inactivos
    List<User> findByActivo(boolean activo);

    // 5. Obtener usuarios ordenados por fecha de creación (más recientes primero)
    List<User> findAllByOrderByFechaCreacionDesc();
    
    // 6. Buscar por email exacto
    Optional<User> findByEmail(String email);
    
    // 7. Verificar si existe un usuario por email
    boolean existsByEmail(String email);
    
    // CONSULTAS PERSONALIZADAS CON @Query
    
    // 8. Contar usuarios activos vs inactivos (usa la propiedad 'activo')
    @Query("SELECT COUNT(u) FROM User u WHERE u.activo = :active")
    long countByActiveStatus(@Param("active") boolean active);
    
    // 9. Buscar usuarios por dominio de email
    @Query("SELECT u FROM User u WHERE u.email LIKE %:domain")
    List<User> findByEmailDomain(@Param("domain") String domain);
    
    // 10. Obtener estadísticas de usuarios por rol (usa 'rol' y 'nombre')
    @Query("SELECT r.nombre, COUNT(u) FROM User u JOIN u.rol r GROUP BY r.nombre")
    List<Object[]> countUsersByRole();
    
    // 11. Buscar usuarios con rol específico y activos
    @Query("SELECT u FROM User u WHERE u.rol.nombre = :roleName AND u.activo = :active")
    List<User> findByRolNombreAndActivo(@Param("roleName") String roleName, @Param("active") boolean active);
    
    // 12. Obtener últimos N usuarios registrados (tabla y columnas en español)
    @Query(value = "SELECT * FROM usuario ORDER BY fecha_creacion DESC LIMIT :limit", nativeQuery = true)
    List<User> findLatestUsers(@Param("limit") int limit);
    
    // 13. Buscar usuarios con nombre que empiece con...
    List<User> findByNombreStartingWith(String prefix);

    // 14. Buscar usuarios con nombre que termine con...
    List<User> findByNombreEndingWith(String suffix);
    
    // 15. Eliminar usuarios inactivos por más de X días (usa 'activo' y 'fechaCreacion')
    @Query("DELETE FROM User u WHERE u.activo = false AND u.fechaCreacion < :date")
    int deleteInactiveUsersBefore(@Param("date") LocalDateTime date);
}