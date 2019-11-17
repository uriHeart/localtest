package com.argo.common.domain.auth;

import com.argo.common.domain.user.ArgoUser;
import com.argo.common.domain.user.UserStatus;
import com.argo.common.exception.UserRegistrationException;
import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.joda.time.LocalDateTime;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user_confirm", schema = "public")
public class UserConfirm {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="user_confirm_seq")
    @SequenceGenerator(name="user_confirm_seq", sequenceName="user_confirm_seq", allocationSize=1)
    @Column(name = "user_confirm_id", nullable = false)
    private Long userConfirmId;

    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "argo_user_id", referencedColumnName = "argo_user_id")
    private ArgoUser argoUser;

    private String uuid;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date ttl;

    @CreationTimestamp
    @Column(name = "created_at")
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Date updatedAt;

    public void userRegistrationConfirm() {
        if (LocalDateTime.now().toDate().after(ttl)) {
            throw new UserRegistrationException("이메일 인증 기간이 만료되었습니다.");
        }
        ArgoUser argoUser = getArgoUser();
        argoUser.setStatus(UserStatus.AUTHENTICATED);
    }
}
