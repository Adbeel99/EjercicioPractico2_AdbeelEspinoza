/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.adbeel.demo.service.dto;

import java.time.LocalDateTime;

public class UserResponseDTO {
    
    private Long id;
    private String nombre;
    private String apellido;
    private String email;
    private boolean activo;
    private LocalDateTime fechaCreacion;
    private String rolNombre;
    private Long rolId;
    
    // Constructores
    public UserResponseDTO() {}
    
    public UserResponseDTO(Long id, String nombre, String apellido, String email, boolean activo, 
                          LocalDateTime fechaCreacion, String rolNombre, Long rolId) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.activo = activo;
        this.fechaCreacion = fechaCreacion;
        this.rolNombre = rolNombre;
        this.rolId = rolId;
    }
    
    // Getters y Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public String getApellido() {
        return apellido;
    }
    
    public void setApellido(String apellido) {
        this.apellido = apellido;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public boolean isActivo() {
        return activo;
    }
    
    public void setActivo(boolean activo) {
        this.activo = activo;
    }
    
    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }
    
    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
    
    public String getRolNombre() {
        return rolNombre;
    }
    
    public void setRolNombre(String rolNombre) {
        this.rolNombre = rolNombre;
    }
    
    public Long getRolId() {
        return rolId;
    }
    
    public void setRolId(Long rolId) {
        this.rolId = rolId;
    }

    // English-named aliases
    public String getName() { return getNombre(); }
    public void setName(String name) { setNombre(name); }

    public boolean isActive() { return isActivo(); }
    public void setActive(boolean active) { setActivo(active); }

    public java.time.LocalDateTime getCreatedAt() { return getFechaCreacion(); }
    public void setCreatedAt(java.time.LocalDateTime createdAt) { setFechaCreacion(createdAt); }

    public String getRoleName() { return getRolNombre(); }
    public void setRoleName(String roleName) { setRolNombre(roleName); }

    public Long getRoleId() { return getRolId(); }
    public void setRoleId(Long roleId) { setRolId(roleId); }
    
    // Método helper
    public String getNombreCompleto() {
        return this.nombre + " " + this.apellido;
    }
    
    @Override
    public String toString() {
        return "UserResponseDTO{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", apellido='" + apellido + '\'' +
                ", email='" + email + '\'' +
                ", activo=" + activo +
                ", fechaCreacion=" + fechaCreacion +
                ", rolNombre='" + rolNombre + '\'' +
                ", rolId=" + rolId +
                '}';
    }
}
