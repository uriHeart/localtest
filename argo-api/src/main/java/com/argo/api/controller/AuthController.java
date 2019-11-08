package com.argo.api.controller;

import com.argo.api.auth.LoginParams;
import com.argo.api.auth.RsaKeyGenerator;
import com.argo.common.domain.user.AddUserForm;
import com.argo.common.domain.user.UserService;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(value = "/api")
@RestController
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Autowired
    private RsaKeyGenerator rsaKeyGenerator;

    @GetMapping(value = "/auth/key")
    public String getPublicKey() throws NoSuchAlgorithmException {
        return rsaKeyGenerator.getPublicKey();
    }

    @PostMapping(value = "/auth/login")
    public ResponseEntity<Object> login(@RequestBody LoginParams params, HttpSession httpSession) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(params.getLoginId(), params.getPassword());
        try {
            Authentication authentication = authenticationManager.authenticate(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            httpSession.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());
            return ResponseEntity.ok(true);
        } catch (Exception e) {
            return ResponseEntity.ok(false);
        }
    }

    @PostMapping(value = "/auth/seller-register")
    public void addUser(@RequestBody AddUserForm addUserForm, HttpSession httpSession) {
        userService.addSeller(addUserForm, httpSession.getAttribute("_RSA_WEB_Key_").toString());
    }

    @GetMapping(value = "/auth/confirm/{uuid}")
    public void confirmUser(@PathVariable String uuid, HttpServletResponse response) throws IOException {
        userService.confirmUser(uuid);
        response.sendRedirect("some-url");
//        return "Verification completed.";
    }
}
