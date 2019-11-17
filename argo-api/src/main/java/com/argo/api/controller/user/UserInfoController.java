package com.argo.api.controller.user;

import com.argo.api.service.gopotal.BusinessNumberCheckService;
import com.argo.common.aws.S3Service;
import com.argo.common.domain.user.AdditionalInfoAddForm;
import com.argo.common.domain.user.UserAdditionalInfoService;
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

    @Autowired
    private S3Service s3Service;

    @GetMapping("/businessLicenseNumber/{number}/companyName/{companyName}")
    public boolean checkBusinessLicenseNumber(@PathVariable String number, @PathVariable String companyName) {
        return businessNumberChecker.checkNumber(number, companyName, 1);
    }

    @PostMapping(value = "/additionalInfo")
    public ResponseEntity<String> additionalInfo(AdditionalInfoAddForm form, Principal principal) {
        s3Service.uploadFile(form.getFileLocation(), form.getFileName(), form.getFile());
        userAdditionalInfoService.addAdditionalInfo(form, principal.getName());
        return new ResponseEntity<>("Created", HttpStatus.OK);
    }
}
