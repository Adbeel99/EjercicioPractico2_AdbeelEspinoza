/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.adbeel.demo.repository.impl;

/**
 *
 * @author Laboratorio
 */
import com.adbeel.demo.repository.UserCustomRepository;
import com.adbeel.demo.domain.User;
import com.adbeel.demo.repository.UserCustomRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class UserCustomRepositoryImpl implements UserCustomRepository {
    
    @PersistenceContext
    private EntityManager entityManager;
    
    @Override
    public Page<User> searchUsers(String keyword, String role, Boolean active, 
                                 LocalDateTime startDate, LocalDateTime endDate, 
                                 Pageable pageable) {
        
        // Implementación con Criteria API para mayor flexibilidad
        // (Puedo desarrollarla si la necesitas)
        
        return new PageImpl<>(List.of(), pageable, 0);
    }

    @Override
    public List<UserProjection> findAllWithDetails() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}