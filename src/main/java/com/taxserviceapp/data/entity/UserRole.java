package com.taxserviceapp.data.entity;

import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;

public enum UserRole implements GrantedAuthority {
    USER("USER"), INSPECTOR("INSPECTOR");

    private final String role;

    UserRole(String role) {
        this.role = role;
    }

    @Override
    public String getAuthority() {
        return role;
    }
}
