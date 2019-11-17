package com.argo.api.controller;

import com.argo.api.auth.AuthUser;
import com.argo.api.auth.LoginParams;
import com.argo.api.auth.LoginResult;
import com.argo.api.auth.RsaKeyGenerator;
import com.argo.api.auth.UserManager;
import com.argo.common.domain.user.AddUserForm;
import com.argo.common.domain.user.UserService;
import java.security.NoSuchAlgorithmException;
import javax.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

@Slf4j
@RequestMapping(value = "/api")
@RestController
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Autowired
    private RsaKeyGenerator rsaKeyGenerator;

//    @Autowired
    private UserManager userManager;

    @GetMapping(value = "/auth/key")
    public String getPublicKey() throws NoSuchAlgorithmException {
        return rsaKeyGenerator.getPublicKey();
    }

    private AuthUser get() {
        try {
            if (SecurityContextHolder.getContext().getAuthentication().getAuthorities().isEmpty()) {
                return null;
            }
            return (AuthUser) SecurityContextHolder.getContext().getAuthentication().getAuthorities().iterator().next();
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    @GetMapping(value = "/check/auth")
    public ResponseEntity<LoginResult> check() {
        AuthUser user = this.get();
        if (user == null) {
            return new ResponseEntity<>(LoginResult.builder().success(false).message("로그인이 되지 않았습니다.").build(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(LoginResult.builder()
                    .success(true)
                    .vendorId(user.getVendorId())
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
