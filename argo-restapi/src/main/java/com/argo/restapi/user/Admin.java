package com.argo.restapi.user;

import com.argo.restapi.auth.RoleType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@DiscriminatorValue("admin")
@EqualsAndHashCode(callSuper = true)
public class Admin extends ArgoUser {

    @Override
    public String getAuthority() {
        return RoleType.ADMIN.name();
    }
}
