package com.argo.common.domain.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class AdditionalInfoAddForm {

    private static final String FILE_LOCATION = "/user-additional-info/license/";
    private static ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private String businessType;
    private String licenseNumber; // 사업자 번호
    private String representativeName;  //대표자 이름
    private String postCode;
    private String baseAddress; // 시군구 주소
    private String detailAddress; // 나머지 주소
    private String saleForm; // 업태
    private String saleType; // 업종
    private MultipartFile file;

    public UserAdditionalInfo getEntity(ArgoUser user) {
        UserAdditionalInfo userAdditionalInfo = new UserAdditionalInfo();
        userAdditionalInfo.setBusinessType(businessType);
        userAdditionalInfo.setLicenseNumber(licenseNumber);
        userAdditionalInfo.setRepresentativeName(representativeName);
        userAdditionalInfo.setPostCode(postCode);
        userAdditionalInfo.setBaseAddress(baseAddress);
        userAdditionalInfo.setDetailAddress(detailAddress);
        userAdditionalInfo.setSaleForm(saleForm);
        userAdditionalInfo.setSaleType(saleType);
        userAdditionalInfo.setLicenseLocation(FILE_LOCATION);
        userAdditionalInfo.setFileName(licenseNumber + representativeName + "." + getFileExtension());
        userAdditionalInfo.setArgoUser(user);

        return userAdditionalInfo;
    }

    private String getFileExtension() {
        String originalFilename = file.getOriginalFilename();
        if (originalFilename != null) {
            return originalFilename.substring(originalFilename.lastIndexOf('.') + 1);
        }

        throw new IllegalArgumentException("file 이름이 잘못되었습니다.");
    }
}
