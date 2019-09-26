package com.argo.common.domain.user;

import lombok.Data;

@Data
public class AddUserForm {

    private String loginId;
    private String password;

    public Seller toSellerEntity() {
        Seller newSeller = new Seller();
        newSeller.setLoginId(this.loginId);
        newSeller.setPassword(this.password);
        newSeller.setUserName(this.loginId);
        return newSeller;
    }
}
