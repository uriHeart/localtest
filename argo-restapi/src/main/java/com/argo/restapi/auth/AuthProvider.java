package com.argo.restapi.auth;

import com.argo.restapi.user.ArgoUser;
import com.argo.restapi.user.UserService;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
public class AuthProvider implements AuthenticationProvider {

    @Autowired
    private UserService userService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String id = authentication.getName();
//        String password = HashUtil.sha256(authentication.getCredentials().toString());
        String password = authentication.getCredentials().toString();
        ArgoUser user = userService.getUserByLoginId(id);
        if (user == null || !password.equals(user.getPassword())) {
            return null;
        }
        return new UsernamePasswordAuthenticationToken(id, password, Lists.newArrayList(user));
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

}
