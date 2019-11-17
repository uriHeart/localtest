package com.argo.common.domain.auth;

import com.argo.common.domain.user.Seller;
import com.argo.common.domain.user.UserStatus;
import org.junit.Assert;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

//import com.argo.common.exception.UserAlreadyApprovedException;

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
        user.setStatus(UserStatus.INITIALIZED);
        authService.createUuidForConfirm(user);
    }

//    @Test
    public void createConfirmData() {
        Seller user = new Seller();
        user.setStatus(UserStatus.INITIALIZED);

        String uuidForConfirm = authService.createUuidForConfirm(user);
        Mockito.verify(userConfirmsRepository).save(argCaptor.capture());

        Assert.assertNotNull(uuidForConfirm);
        Assert.assertEquals(uuidForConfirm, argCaptor.getValue().getUuid());
        Assert.assertEquals(user, argCaptor.getValue().getArgoUser());
    }
}
