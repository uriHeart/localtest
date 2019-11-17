package com.argo.common.domain.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserAdditionalInfoService {

    @Autowired
    private UserAdditionalInfoRepository userAdditionalInfoRepository;

    @Autowired
    private UserService userService;

    @Transactional
    public void addAdditionalInfo(AdditionalInfoAddForm form, String userId) {
        UserAdditionalInfo info = form.getEntity(userService.getUserByLoginId(userId));
        ArgoUser targetUser = info.getArgoUser();
        targetUser.setStatus(UserStatus.ACTIVATED);
        userAdditionalInfoRepository.save(info);
    }
}
