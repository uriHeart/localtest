package com.argo.api.controller.user;

import com.argo.api.service.gopotal.BusinessNumberCheckService;
import com.argo.common.domain.user.AdditionalInfoAddForm;
import com.argo.common.domain.user.UserAdditionalInfoService;
import java.net.URISyntaxException;
import java.security.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/userInfo")
public class UserInfoController {

    @Autowired
    private UserAdditionalInfoService userAdditionalInfoService;

    @Autowired
    private BusinessNumberCheckService businessNumberChecker;

    @GetMapping("/businessLicenseNumber/{number}/companyName/{companyName}")
    public boolean checkBusinessLicenseNumber(@PathVariable String number, @PathVariable String companyName) throws URISyntaxException {
        return businessNumberChecker.checkNumber(number, companyName, 1);
    }


    @PostMapping(value = "/additionalInfo")
    public ResponseEntity<String> additionalInfo(AdditionalInfoAddForm form, Principal principal) {
        userAdditionalInfoService.addAdditionalInfo(form, principal.getName());
        return new ResponseEntity<>("Created", HttpStatus.OK);
    }
}
