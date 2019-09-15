package com.argo.restapi.auth;

import com.argo.restapi.user.ArgoUser;
import com.argo.restapi.user.UserService;
import com.argo.restapi.util.HashUtil;
import com.google.common.collect.Lists;
import javax.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AuthProvider implements AuthenticationProvider {

    @Autowired
    private UserService userService;

    @Autowired
    private HttpSession httpSession;

    @Autowired
    private RsaDecrypter rsaDecrypter;

    @Override
    public Authentication authenticate(Authentication authentication) {
        try {
            String loginId = authentication.getName();
            String password = rsaDecrypter.decryptRsa(authentication.getCredentials().toString(), httpSession.getAttribute("_RSA_WEB_Key_").toString());
            ArgoUser user = userService.getUserByLoginId(loginId);
            if (user == null || !HashUtil.sha256(password).equals(user.getPassword())) {
                return null;
            }
            return new UsernamePasswordAuthenticationToken(loginId, password, Lists.newArrayList(user));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

}
