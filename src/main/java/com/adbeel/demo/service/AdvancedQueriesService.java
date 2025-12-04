/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */

package com.adbeel.demo.service;

/**
 *
 * @author Laboratorio
 */
import com.adbeel.demo.domain.User;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface AdvancedQueriesService {
    
    // Métodos para la página de consultas avanzadas
    List<User> getUsersByRole(String roleName);
    List<User> getUsersByDateRange(LocalDateTime startDate, LocalDateTime endDate);
    List<User> getUsersByPartialMatch(String searchTerm);
    List<User> getUsersOrderedByCreationDate();
    Map<String, Object> getUserStatistics();
    List<Object[]> getUsersByRoleWithCount();
    List<User> getLatestUsers(int limit);
    List<User> getActiveUsers();
    List<User> getInactiveUsers();
}
