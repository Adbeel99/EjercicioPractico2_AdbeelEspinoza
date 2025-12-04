/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.adbeel.demo.service.dto;

/**
 *
 * @author Laboratorio
 */
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class UserResponseDTO {
    private Long id;
    private String name;
    private String email;
    private boolean active;
    private LocalDateTime createdAt;
    private String roleName;
    private Long roleId;
}
