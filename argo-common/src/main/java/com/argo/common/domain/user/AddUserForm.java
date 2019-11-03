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
        return UserMapper.INSTANCE.userFormToSeller(this);
    }
}
