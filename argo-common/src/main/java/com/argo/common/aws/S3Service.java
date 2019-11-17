package com.argo.common.aws;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import java.io.File;
import java.io.IOException;
import java.util.Objects;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
public class S3Service {

    @Value("${aws.s3.accessKey}")
    private String ACCESS_KEY;

    @Value("${aws.s3.secretKey}")
    private String SECRET_KEY;

    private AmazonS3 amazonS3;

    @PostConstruct
    public void initClient() {
        AWSCredentials awsCredentials = new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY);
        amazonS3 = AmazonS3ClientBuilder.standard()
            .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
            .withRegion(Regions.AP_NORTHEAST_2)
            .build();
    }

    public void uploadFile(String path, String fileName, MultipartFile file) {
        try {
            File uploadFile = convert(file);
            PutObjectRequest putObjectRequest = new PutObjectRequest(path, fileName, uploadFile);
            putObjectRequest.setCannedAcl(CannedAccessControlList.AuthenticatedRead);
//            amazonS3.putObject(putObjectRequest);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    private File convert(MultipartFile file) throws IOException {
        File convertFile = new File(Objects.requireNonNull(file.getOriginalFilename()));
        file.transferTo(convertFile);
        return convertFile;
    }
}
