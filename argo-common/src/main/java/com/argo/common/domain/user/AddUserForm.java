package com.argo.common.domain.user;

import lombok.Data;

@Data
public class AddUserForm {

    private String email;
    private String password;
    private String company;
    private String managerName;
    private String phoneNumber;
    private boolean isApproved;

    public Seller toSellerEntity() {
        Seller newSeller = new Seller();
        newSeller.setLoginId(this.email);
        newSeller.setPassword(this.password);
        newSeller.setUserName(this.managerName);
        newSeller.setCompanyName(this.company);
        newSeller.setPhoneNumber(this.phoneNumber);
        newSeller.setApproved(false);
        return newSeller;
    }
}
