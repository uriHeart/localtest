package com.argo.restapi.user;

import com.argo.restapi.auth.RoleType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@DiscriminatorValue("seller")
@EqualsAndHashCode(callSuper = true)
public class Seller extends ArgoUser {

    @Override
    public String getAuthority() {
        return RoleType.SELLER.name();
    }
}
