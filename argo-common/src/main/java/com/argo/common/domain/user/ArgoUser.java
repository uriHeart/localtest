package com.argo.common.domain.user;

import com.argo.common.domain.auth.RoleType;
import com.argo.common.domain.common.jpa.CreatedAtListener;
import com.argo.common.domain.common.jpa.SystemMetadata;
import com.argo.common.domain.common.jpa.UpdatedAtListener;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Data
@Builder
@Entity
@Table(name = "argo_users", schema = "public")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@EntityListeners( { CreatedAtListener.class, UpdatedAtListener.class } )
public class ArgoUser implements SystemMetadata {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="argo_users_seq")
    @SequenceGenerator(name="argo_users_seq", sequenceName="argo_users_seq", allocationSize=1)
    @Column(name = "user_id", nullable = false)
    private Long argoUserId;

    @Column(name = "login_id")
    private String loginId;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "password")
    private String password;

    @OneToMany
    @JoinColumn(name="role_id")
    private final List<Role> roles = new ArrayList<>();

    public Collection<Role> getRoles() {
        return roles;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at", nullable = false)
    private Date updatedAt;

}
