package com.argo.api.auth;


import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

//@Component
public class UserManager {
    public AuthUser get() {
        if (SecurityContextHolder.getContext().getAuthentication().getAuthorities().isEmpty()) {
            return null;
        }
        return (AuthUser) SecurityContextHolder.getContext().getAuthentication().getAuthorities().iterator().next();
    }
}
