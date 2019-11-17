package com.argo.common.domain.auth;

import com.argo.common.domain.user.ArgoUser;
import com.argo.common.domain.user.UserStatus;
import com.argo.common.exception.UserRegistrationException;
import org.joda.time.LocalDateTime;
import org.junit.Assert;
import org.junit.Test;

public class UserConfirmTest {

    @Test
    public void userRegistrationConfirm() {
        ArgoUser argoUser = new ArgoUser();
        argoUser.setStatus(UserStatus.INITIALIZED);

        UserConfirm userConfirm = new UserConfirm();
        userConfirm.setTtl(LocalDateTime.now().plusHours(2).toDate());
        userConfirm.setArgoUser(argoUser);

        userConfirm.userRegistrationConfirm();

        Assert.assertEquals(userConfirm.getArgoUser().getStatus(), UserStatus.AUTHENTICATED);
    }

    @Test(expected = UserRegistrationException.class)
    public void userRegistrationConfirm_TimeOut() {
        ArgoUser argoUser = new ArgoUser();
        argoUser.setStatus(UserStatus.INITIALIZED);

        UserConfirm userConfirm = new UserConfirm();
        userConfirm.setTtl(LocalDateTime.now().minusHours(2).toDate());
        userConfirm.setArgoUser(argoUser);

        userConfirm.userRegistrationConfirm();
    }
}
