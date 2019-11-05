package com.argo.common.domain.user.password;

import com.argo.common.domain.common.jpa.CreatedAtListener;
import com.argo.common.domain.common.jpa.SystemMetadata;
import com.argo.common.domain.common.jpa.UpdatedAtListener;
import com.argo.common.domain.user.ArgoUser;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Date;

@Data
@Builder
@Entity
@javax.persistence.Table(name = "password_recoveries", schema = "public")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@EntityListeners( { CreatedAtListener.class, UpdatedAtListener.class } )
public class PasswordRecovery implements SystemMetadata {
    public static final int MAX_TOKEN_SIZE = 256;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "password_recovery_seq")
    @SequenceGenerator(name = "password_recovery_seq", sequenceName = "password_recovery_seq", allocationSize = 1)
    @javax.persistence.Column(name = "password_recovery_id", nullable = false)
    private Long passwordRecoveryId;

    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="user_id")
    private ArgoUser user;

    @Size(max = 256)
    @Column(name = "token")
    private String token;

    @Column(name = "active")
    private Boolean active;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at", nullable = false)
    private Date updatedAt;
}