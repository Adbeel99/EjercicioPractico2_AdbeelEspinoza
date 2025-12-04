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

import org.springframework.stereotype.Repository;

import com.adbeel.demo.domain.User;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

@Repository
public class CustomQueries {
    
    @PersistenceContext
    private EntityManager entityManager;
    
    // Consulta personalizada compleja: usuarios con filtros múltiples
    public List<User> findUsersWithAdvancedFilters(String name, String email, String roleName, 
                                                   LocalDateTime startDate, LocalDateTime endDate, 
                                                   Boolean active) {
        
        StringBuilder jpql = new StringBuilder("SELECT u FROM User u WHERE 1=1");
        
        if (name != null && !name.isEmpty()) {
            jpql.append(" AND LOWER(u.nombre) LIKE LOWER(CONCAT('%', :name, '%'))");
        }
        
        if (email != null && !email.isEmpty()) {
            jpql.append(" AND LOWER(u.email) LIKE LOWER(CONCAT('%', :email, '%'))");
        }
        
        if (roleName != null && !roleName.isEmpty()) {
            jpql.append(" AND u.rol.nombre = :roleName");
        }
        
        if (startDate != null) {
            jpql.append(" AND u.fechaCreacion >= :startDate");
        }
        
        if (endDate != null) {
            jpql.append(" AND u.fechaCreacion <= :endDate");
        }
        
        if (active != null) {
            jpql.append(" AND u.activo = :active");
        }
        
        jpql.append(" ORDER BY u.fechaCreacion DESC");
        
        TypedQuery<User> query = entityManager.createQuery(jpql.toString(), User.class);
        
        if (name != null && !name.isEmpty()) {
            query.setParameter("name", name);
        }
        
        if (email != null && !email.isEmpty()) {
            query.setParameter("email", email);
        }
        
        if (roleName != null && !roleName.isEmpty()) {
            query.setParameter("roleName", roleName);
        }
        
        if (startDate != null) {
            query.setParameter("startDate", startDate);
        }
        
        if (endDate != null) {
            query.setParameter("endDate", endDate);
        }
        
        if (active != null) {
            query.setParameter("active", active);
        }
        
        return query.getResultList();
    }
    
    // Obtener resumen de actividad de usuarios
    public Object[] getUserActivitySummary() {
        String jpql = """
            SELECT 
                COUNT(u) as totalUsers,
                SUM(CASE WHEN u.activo = true THEN 1 ELSE 0 END) as activeUsers,
                SUM(CASE WHEN u.activo = false THEN 1 ELSE 0 END) as inactiveUsers,
                MIN(u.fechaCreacion) as firstRegistration,
                MAX(u.fechaCreacion) as lastRegistration
            FROM User u
            """;
        
        return (Object[]) entityManager.createQuery(jpql).getSingleResult();
    }
}