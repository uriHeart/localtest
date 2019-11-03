package com.argo.common.domain.user;

import com.argo.common.domain.auth.RoleType;
import com.google.common.collect.Lists;
import java.util.List;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@DiscriminatorValue("seller")
@EqualsAndHashCode(callSuper = true)
public class Seller extends ArgoUser {

    private String companyName;
    private String phoneNumber;
    private boolean isApproved;

    public List<Role> getRoles() {
        return Lists.newArrayList(Role.builder().roleId(RoleType.SELLER.name()).build());
    }
}
