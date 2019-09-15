package com.argo.common.domain.user;

import com.argo.common.domain.common.jpa.CreatedAtListener;
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
@Table(name = "roles", schema = "public")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class Role implements GrantedAuthority {
    @Id
    @Column(name = "role_id", nullable = false)
    private String roleId;

    @OneToMany
    @JoinColumn(name="operation_id")
    private final List<Operation> allowedOperations = new ArrayList<>();

    @Override
    public String getAuthority() {
        return roleId;
    }

    public List<Operation> getAllowedOperations() {
        return allowedOperations;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at", nullable = false)
    private Date updatedAt;
}
