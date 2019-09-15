package com.argo.restapi.controller;

import com.argo.restapi.auth.LoginParams;
import com.argo.restapi.auth.PublicKeyDto;
import com.argo.restapi.auth.RsaKeyGenerator;
import com.argo.restapi.user.AddUserForm;
import com.argo.restapi.user.UserService;
import java.security.NoSuchAlgorithmException;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Autowired
    private RsaKeyGenerator rsaKeyGenerator;

    @PostMapping(value = "/api/auth/login")
    public ResponseEntity<Object> login(@RequestBody LoginParams params, HttpSession session) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(params.getLoginId(), params.getPassword());
        try {
            Authentication authentication = authenticationManager.authenticate(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());
            return ResponseEntity.ok(true);
        } catch (Exception e) {
            return ResponseEntity.ok(false);
        }
    }

    @PostMapping(value = "/api/auth/seller-register")
    public void addUser(@RequestBody AddUserForm addUserForm) {
        userService.addSeller(addUserForm);
    }

    @GetMapping(value = "/api/auth/key")
    public PublicKeyDto getPublicKey() throws NoSuchAlgorithmException {
        return rsaKeyGenerator.getPublicKey();
    }
}
