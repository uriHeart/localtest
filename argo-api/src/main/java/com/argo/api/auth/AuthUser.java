package com.argo.api.auth;

import com.argo.common.domain.user.Role;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthUser implements GrantedAuthority {

    private String loginId;
    private String userName;
    private String password;
    private List<Role> roles;

    @Override
    public String getAuthority() {
        return RoleType.SELLER.name();
    }
}
