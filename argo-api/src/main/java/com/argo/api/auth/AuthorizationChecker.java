package com.argo.api.auth;

import javax.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

@Component
public class AuthorizationChecker {

    private static final GrantedAuthority ADMIN = new SimpleGrantedAuthority("ROLE_ADMIN");

    public boolean check(HttpServletRequest request, Authentication authentication) {
        if (authentication.getAuthorities().contains(ADMIN)) {
            return true;
        }
        return true;
    }
}
