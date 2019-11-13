package com.argo.api.auth;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@Scope(value="session", proxyMode= ScopedProxyMode.TARGET_CLASS)
public class UserManager {
    public AuthUser get() {
        if (SecurityContextHolder.getContext().getAuthentication().getAuthorities().isEmpty()) {
            return null;
        }
        return (AuthUser) SecurityContextHolder.getContext().getAuthentication().getAuthorities().iterator().next();
    }
}
