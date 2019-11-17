package com.argo.common.domain.user;

import com.argo.common.aws.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserAdditionalInfoService {

    @Autowired
    private UserAdditionalInfoRepository userAdditionalInfoRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private S3Service s3Service;

    public void addAdditionalInfo(AdditionalInfoAddForm form, String userId) {
        UserAdditionalInfo info = form.getEntity(userService.getUserByLoginId(userId));
        s3Service.uploadFile(info.getFileLocation(), info.getFileName(), form.getFile());
        userAdditionalInfoRepository.save(info);
    }
}
