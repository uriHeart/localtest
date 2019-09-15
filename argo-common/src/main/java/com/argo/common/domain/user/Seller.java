package com.argo.common.domain.user;

import com.argo.common.domain.auth.RoleType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.testng.collections.Lists;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.util.List;

@Data
@Entity
@DiscriminatorValue("seller")
@EqualsAndHashCode(callSuper = true)
public class Seller extends ArgoUser {

    public List<Role> getRoles() {
        return Lists.newArrayList(Role.builder().roleId(RoleType.SELLER.name()).build());
    }
}
