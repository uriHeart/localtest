package com.argo.api.auth;

import com.argo.common.domain.common.util.HashUtil;
import com.argo.common.domain.user.Role;
import com.argo.common.domain.user.UserStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
    private UserStatus userStatus;
    private List<Role> roles;
    private Long vendorId;

    @Override
    public String getAuthority() {
        return RoleType.SELLER.name();
    }

    public Long getVendorId() {
        return this.vendorId == null ? 0L : this.vendorId;
    }

    public boolean confirmPassword(String password) {
        return HashUtil.sha256(password).equals(getPassword());
    }
}
