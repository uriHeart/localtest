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

    public List<Role> getRoles() {
        return Lists.newArrayList(Role.builder().roleId(RoleType.SELLER.name()).build());
    }
}
