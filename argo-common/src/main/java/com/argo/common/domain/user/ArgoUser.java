package com.argo.common.domain.user;

import com.argo.common.domain.common.jpa.CreatedAtListener;
import com.argo.common.domain.common.jpa.SystemMetadata;
import com.argo.common.domain.common.jpa.UpdatedAtListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    @Column(name = "argo_user_id", nullable = false)
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
