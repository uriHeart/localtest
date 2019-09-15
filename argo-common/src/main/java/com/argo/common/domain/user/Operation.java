package com.argo.common.domain.user;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.Date;

@Data
@Builder
@Entity
@Table(name = "operations", schema = "public")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class Operation implements GrantedAuthority {
    @Id
    @Column(name = "operation_id", nullable = false)
    private String operationId;

    @Override
    public String getAuthority() {
        return operationId;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at", nullable = false)
    private Date updatedAt;
}
