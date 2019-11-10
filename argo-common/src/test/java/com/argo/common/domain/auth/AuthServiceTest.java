package com.argo.common.domain.auth;

import com.argo.common.domain.user.Seller;
//import com.argo.common.exception.UserAlreadyApprovedException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

//@RunWith(MockitoJUnitRunner.class)
public class AuthServiceTest {

    @InjectMocks
    private AuthService authService;

    @Mock
    private UserConfirmsRepository userConfirmsRepository;

    @Captor
    private ArgumentCaptor<UserConfirm> argCaptor;

//    @Test(expected = UserAlreadyApprovedException.class)
    public void alreadyApprovedUser() {
        Seller user = new Seller();
        user.setApproved(true);
        authService.createUuidForConfirm(user);
    }

//    @Test
    public void createConfirmData() {
        Seller user = new Seller();
        user.setApproved(false);

        String uuidForConfirm = authService.createUuidForConfirm(user);
        Mockito.verify(userConfirmsRepository).save(argCaptor.capture());

        Assert.assertNotNull(uuidForConfirm);
        Assert.assertEquals(uuidForConfirm, argCaptor.getValue().getUuid());
        Assert.assertEquals(user, argCaptor.getValue().getArgoUser());
    }
}
