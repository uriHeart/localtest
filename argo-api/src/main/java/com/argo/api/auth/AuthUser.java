package com.argo.api.auth;

import com.argo.common.domain.user.Role;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.List;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityCoreVersion;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthUser implements GrantedAuthority {

    private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;

    private String loginId;
    private String userName;
    private String password;
    private List<Role> roles;
    private Long vendorId;

    @Override
    public String getAuthority() {
        return RoleType.SELLER.name();
    }
}
