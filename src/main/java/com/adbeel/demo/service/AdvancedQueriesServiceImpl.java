/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.adbeel.demo.service;

/**
 *
 * @author Laboratorio
 */
import com.adbeel.demo.domain.User;
import com.adbeel.demo.repository.UserRepository;
import com.adbeel.demo.service.AdvancedQueriesService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AdvancedQueriesServiceImpl implements AdvancedQueriesService {
    
    private final UserRepository userRepository;
    
    @Override
    public List<User> getUsersByRole(String roleName) {
        return userRepository.findByRoleName(roleName);
    }
    
    @Override
    public List<User> getUsersByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return userRepository.findByCreatedAtBetween(startDate, endDate);
    }
    
    @Override
    public List<User> getUsersByPartialMatch(String searchTerm) {
        return userRepository.findByEmailContainingOrNameContaining(searchTerm, searchTerm);
    }
    
    @Override
    public List<User> getUsersOrderedByCreationDate() {
        return userRepository.findAllByOrderByCreatedAtDesc();
    }
    
    @Override
    public Map<String, Object> getUserStatistics() {
        Map<String, Object> stats = new HashMap<>();
        
        long totalUsers = userRepository.count();
        long activeUsers = userRepository.countByActiveStatus(true);
        long inactiveUsers = userRepository.countByActiveStatus(false);
        
        stats.put("totalUsers", totalUsers);
        stats.put("activeUsers", activeUsers);
        stats.put("inactiveUsers", inactiveUsers);
        stats.put("activePercentage", totalUsers > 0 ? (activeUsers * 100.0 / totalUsers) : 0);
        stats.put("inactivePercentage", totalUsers > 0 ? (inactiveUsers * 100.0 / totalUsers) : 0);
        
        // Obtener estadísticas por rol
        List<Object[]> roleStats = userRepository.countUsersByRole();
        Map<String, Long> usersByRole = new HashMap<>();
        for (Object[] stat : roleStats) {
            usersByRole.put((String) stat[0], (Long) stat[1]);
        }
        stats.put("usersByRole", usersByRole);
        
        return stats;
    }
    
    @Override
    public List<Object[]> getUsersByRoleWithCount() {
        return userRepository.countUsersByRole();
    }
    
    @Override
    public List<User> getLatestUsers(int limit) {
        return userRepository.findLatestUsers(limit);
    }
    
    @Override
    public List<User> getActiveUsers() {
        return userRepository.findByActive(true);
    }
    
    @Override
    public List<User> getInactiveUsers() {
        return userRepository.findByActive(false);
    }
}
