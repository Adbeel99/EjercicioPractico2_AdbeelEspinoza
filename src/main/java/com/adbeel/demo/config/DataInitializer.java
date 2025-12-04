/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.adbeel.demo.config;

/**
 *
 * @author Laboratorio
 */
import com.adbeel.demo.domain.Role;
import com.adbeel.demo.domain.User;
import com.adbeel.demo.repository.RoleRepository;
import com.adbeel.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class DataInitializer {
    
    @Bean
    CommandLineRunner initDatabase(RoleRepository roleRepository, 
                                   UserRepository userRepository, 
                                   PasswordEncoder passwordEncoder) {
        return args -> {
            log.info("Inicializando datos de la aplicación...");
            
            // Crear roles si no existen
            List<String> roleNames = Arrays.asList("ADMIN", "PROFESOR", "ESTUDIANTE");
            
            for (String roleName : roleNames) {
                if (roleRepository.findByName(roleName).isEmpty()) {
                    Role role = new Role();
                    role.setName(roleName);
                    roleRepository.save(role);
                    log.info("Rol creado: {}", roleName);
                }
            }
            
            // Crear usuario admin si no existe
            if (userRepository.findByEmail("admin@adbeel.com").isEmpty()) {
                Role adminRole = roleRepository.findByName("ADMIN")
                    .orElseThrow(() -> new RuntimeException("Rol ADMIN no encontrado"));
                
                User admin = new User();
                admin.setName("Administrador Principal");
                admin.setEmail("admin@adbeel.com");
                admin.setPassword(passwordEncoder.encode("admin123"));
                admin.setActive(true);
                admin.setRole(adminRole);
                
                userRepository.save(admin);
                log.info("Usuario administrador creado: admin@adbeel.com / admin123");
            }
            
            // Crear usuario profesor de ejemplo
            if (userRepository.findByEmail("profesor@adbeel.com").isEmpty()) {
                Role profesorRole = roleRepository.findByName("PROFESOR")
                    .orElseThrow(() -> new RuntimeException("Rol PROFESOR no encontrado"));
                
                User profesor = new User();
                profesor.setName("Profesor Ejemplo");
                profesor.setEmail("profesor@adbeel.com");
                profesor.setPassword(passwordEncoder.encode("profesor123"));
                profesor.setActive(true);
                profesor.setRole(profesorRole);
                
                userRepository.save(profesor);
                log.info("Usuario profesor creado: profesor@adbeel.com / profesor123");
            }
            
            // Crear usuario estudiante de ejemplo
            if (userRepository.findByEmail("estudiante@adbeel.com").isEmpty()) {
                Role estudianteRole = roleRepository.findByName("ESTUDIANTE")
                    .orElseThrow(() -> new RuntimeException("Rol ESTUDIANTE no encontrado"));
                
                User estudiante = new User();
                estudiante.setName("Estudiante Ejemplo");
                estudiante.setEmail("estudiante@adbeel.com");
                estudiante.setPassword(passwordEncoder.encode("estudiante123"));
                estudiante.setActive(true);
                estudiante.setRole(estudianteRole);
                
                userRepository.save(estudiante);
                log.info("Usuario estudiante creado: estudiante@adbeel.com / estudiante123");
            }
            
            log.info("Datos inicializados correctamente");
        };
    }
}
