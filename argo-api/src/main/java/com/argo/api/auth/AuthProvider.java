package com.argo.api.auth;

import com.argo.api.exception.ApiException;
import com.argo.common.domain.auth.RsaDecrypter;
import com.argo.common.domain.user.ArgoUser;
import com.argo.common.domain.user.UserService;
import java.util.Optional;
import javax.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AuthProvider implements AuthenticationProvider {

    private ModelMapper modelMapper = new ModelMapper();

    @Autowired
    private UserService userService;

    @Autowired
    private HttpSession httpSession;

    @Autowired
    private RsaDecrypter rsaDecrypter;

    @Override
    public Authentication authenticate(Authentication authentication) {
        try {
            String loginId = authentication.getName();
            String password = rsaDecrypter.decryptRsa(authentication.getCredentials().toString(), httpSession.getAttribute("_RSA_WEB_Key_").toString());
            ArgoUser user = Optional.ofNullable(userService.getUserByLoginId(loginId)).orElseThrow(new ApiException("등록된 사용자가 아닙니다."));
            AuthUser authUser = modelMapper.map(user, AuthUser.class);
            if (!authUser.confirmPassword(password)) {
                throw new ApiException("비밀번호가 잘못되었습니다.");
            }

            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                loginId, password,
                AuthorityUtils.createAuthorityList("ROLE_USER"));
            token.setDetails(authUser);
            return token;
        } catch (ApiException e) {
            log.error(e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new ApiException("로그인에 실패하였습니다.");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

}
