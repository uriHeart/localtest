package com.argo.api.controller;

import com.argo.api.auth.*;
import com.argo.common.domain.user.AddUserForm;
import com.argo.common.domain.user.UserService;
import java.security.NoSuchAlgorithmException;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.*;

@RequestMapping(value = "/api")
@RestController
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Autowired
    private RsaKeyGenerator rsaKeyGenerator;

    @Autowired
    private UserManager userManager;

    @GetMapping(value = "/auth/key")
    public String getPublicKey() throws NoSuchAlgorithmException {
        return rsaKeyGenerator.getPublicKey();
    }

    private AuthUser get() {
        if (SecurityContextHolder.getContext().getAuthentication().getAuthorities().isEmpty()) {
            return null;
        }
        return (AuthUser) SecurityContextHolder.getContext().getAuthentication().getAuthorities().iterator().next();
    }

    @GetMapping(value = "/auth-check")
    public ResponseEntity<LoginResult> check() {
        AuthUser authUser = this.get();
        if (authUser == null) {
            return new ResponseEntity<>(LoginResult.builder().success(false).message("로그인이 되지 않았습니다.").build(), HttpStatus.OK);
        } else {
            AuthUser user = this.get();
            return new ResponseEntity<>(LoginResult.builder()
                    .success(true)
                    .vendorId(user == null ? 0L : user.getVendorId())
                    .dashboardUrl("https://db.argoport.com:5601/app/kibana#/dashboard/b90faa00-e78c-11e9-8acc-f3cddb6dfb41?embed=true&_g=(refreshInterval%3A(pause%3A!f%2Cvalue%3A5000)%2Ctime%3A(from%3Anow-30d%2Cto%3Anow))")
                    .build(), HttpStatus.OK);
        }
    }

    @PostMapping(value = "/auth/login")
    public ResponseEntity<LoginResult> login(@RequestBody LoginParams params, HttpSession httpSession) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(params.getLoginId(), params.getPassword());
        try {
            Authentication authentication = authenticationManager.authenticate(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            httpSession.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());
            AuthUser user = this.get();
            return new ResponseEntity<>(LoginResult.builder()
                    .success(true)
                    .vendorId(user == null ? 0L : user.getVendorId())
                    .dashboardUrl("https://db.argoport.com:5601/app/kibana#/dashboard/b90faa00-e78c-11e9-8acc-f3cddb6dfb41?embed=true&_g=(refreshInterval%3A(pause%3A!f%2Cvalue%3A5000)%2Ctime%3A(from%3Anow-30d%2Cto%3Anow))")
                    .build(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(LoginResult.builder().success(false).message(e.getMessage()).build(), HttpStatus.OK);
        }
    }

    @PostMapping(value = "/auth/seller-register")
    public void addUser(@RequestBody AddUserForm addUserForm, HttpSession httpSession) {
        userService.addSeller(addUserForm, httpSession.getAttribute("_RSA_WEB_Key_").toString());
    }

    @GetMapping(value = "/auth/confirm/{uuid}")
    public String confirmUser(@PathVariable String uuid) {
        userService.confirmUser(uuid);
        return "User Authentication Completed.";
    }
}
