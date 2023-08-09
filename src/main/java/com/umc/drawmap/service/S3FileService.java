package com.umc.drawmap.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class S3FileService {
    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String upload(MultipartFile file) {

        try {
            String fileName = file.getOriginalFilename();
            String fileUrl = "https://" + bucket + "/test" + fileName;
            ObjectMetadata fileMetaData = new ObjectMetadata();

            fileMetaData.setContentType(file.getContentType());
            fileMetaData.setContentLength(file.getSize());

            amazonS3Client.putObject(bucket, fileName, file.getInputStream(), fileMetaData);
            return fileUrl;
        } catch (IOException ex) {
            ex.printStackTrace();
        } return "Fail";
    }

}
