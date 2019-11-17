package com.argo.api.user;

import com.argo.common.domain.user.ArgoUser;
import com.argo.common.domain.user.UserService;
import com.argo.common.domain.user.password.PasswordRecovery;
import com.argo.common.domain.user.password.PasswordResetForm;
import com.argo.common.domain.user.password.PasswordService;
import java.security.Principal;
import javax.servlet.http.HttpSession;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordService passwordService;

    @GetMapping
    public ResponseEntity<ArgoUser> getUser(String email) {
        return ResponseEntity.ok(userService.getUserByLoginId(email));
    }

    @PutMapping
    public ResponseEntity update(@RequestBody ArgoUser argoUser, Principal principal) {
        // make sure he's changing himself
        if(!argoUser.getLoginId().equals(principal.getName())) {
            return ResponseEntity.badRequest().body("Different user trying to modify another user's info");
        }

        if(!StringUtils.isNotEmpty(argoUser.getPassword())) {
            // password is being changed
            return ResponseEntity.badRequest().body("Password is being changed, user cannot update password info");
        }

        ArgoUser user = userService.updateUser(argoUser, principal.getName());

        return ResponseEntity.ok(user);
    }

    @GetMapping("/passwordRecovery")
    public void passwordRecovery(String email) {
        ArgoUser user = userService.getUserByLoginId(email);
        if(user != null) {
            passwordService.deactivateTokenForUser(user);

        }
    }


    @GetMapping(value = "/recover")
    public ResponseEntity recover(@RequestParam(value = "token") String token) {
        PasswordRecovery passwordRecovery = passwordService.getActivePasswordRecoveryByToken(token);

        if (passwordRecovery == null) {
            return ResponseEntity.notFound().build();
        } else {
            if(passwordService.isTokenValid(passwordRecovery)) {
                return ResponseEntity.ok().build();
            } else {
                passwordService.deactivatePasswordRecovery(passwordRecovery);
                return ResponseEntity.notFound().build();
            }
        }
    }

    @PutMapping(value = "/password/reset")
    public ResponseEntity resetPassword(@RequestBody PasswordResetForm resetForm, Principal principal, HttpSession httpSession) {
        if(resetForm.getNewPassword().equals(resetForm.getNewPasswordConfirm())) {
            return ResponseEntity.badRequest().body("Password does not match");
        }
        ArgoUser user = userService.resetPassword(principal.getName(), resetForm.getNewPassword());
        return ResponseEntity.ok(user);
    }

}
