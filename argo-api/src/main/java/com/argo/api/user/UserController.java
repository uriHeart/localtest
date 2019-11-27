package com.argo.api.user;

import com.argo.api.exception.ApiException;
import com.argo.common.domain.user.ArgoUser;
import com.argo.common.domain.user.UserService;
import com.argo.common.domain.user.password.PasswordRecovery;
import com.argo.common.domain.user.password.PasswordResetForm;
import com.argo.common.domain.user.password.PasswordService;
import java.security.Principal;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
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

    @GetMapping("/password/passwordRecovery/{email:.+}")
    public void passwordRecovery(@PathVariable String email) {
        ArgoUser user = Optional.ofNullable(userService.getUserByLoginId(email)).orElseThrow(new ApiException("등록되지 않은 email 주소입니다."));
        if(user != null) {
            passwordService.deactivateTokenForUser(user);
        }
    }


    @GetMapping(value = "/password/recover")
    public ResponseEntity recover(@RequestParam(value = "token") String token) {
        PasswordRecovery passwordRecovery = passwordService.getActivePasswordRecoveryByToken(token);

        if (passwordRecovery == null) {
            return ResponseEntity.notFound().build();
        } else {
            if(passwordService.isTokenValid(passwordRecovery)) {
                return ResponseEntity.ok().body(passwordRecovery.getArgoUser().getLoginId());
            } else {
                passwordService.deactivatePasswordRecovery(passwordRecovery);
                return ResponseEntity.notFound().build();
            }
        }
    }

    @PutMapping(value = "/password/reset")
    public ResponseEntity resetPassword(@RequestBody PasswordResetForm resetForm) {
        ArgoUser user = userService.resetPassword(resetForm.getLoginId(), resetForm.getNewPassword());
        return ResponseEntity.ok(user);
    }

}
