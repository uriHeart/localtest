package com.argo.common.domain.user;

import com.argo.common.domain.common.util.HashUtil;
import com.argo.common.domain.auth.RsaDecrypter;
import lombok.Data;

@Data
public class AddUserForm {

    private String email;
    private String password;
    private String company;
    private String managerName;
    private String phoneNumber;
    private boolean isApproved;

    public Seller toSellerEntity(RsaDecrypter rsaDecrypter, String rsaPrivateKey) {
        String password = rsaDecrypter.decryptRsa(this.password, rsaPrivateKey);
        Seller newSeller = new Seller();
        newSeller.setLoginId(this.email);
        newSeller.setPassword(HashUtil.sha256(password));
        newSeller.setUserName(this.managerName);
        newSeller.setCompanyName(this.company);
        newSeller.setPhoneNumber(this.phoneNumber);
        newSeller.setApproved(false);
        return newSeller;
    }
}
