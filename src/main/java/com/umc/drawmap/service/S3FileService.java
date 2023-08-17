package com.umc.drawmap.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class S3FileService {
    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String upload(List<MultipartFile> files) {

        // S3 에 저장 되는 파일 이름은  업로드 시각 + 유저 이름 으로 하자.
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = ((UserDetails) authentication.getPrincipal()).getUsername();
        StringBuilder resultBuilder = new StringBuilder();

        if (files == null) {
            return "";
        }

        for (MultipartFile file : files) {
            try (InputStream inputStream = file.getInputStream()) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String nowTime = dateFormat.format(new Date());

                String fileName = nowTime + " " + userId;

                ObjectMetadata fileMetaData = new ObjectMetadata();
                fileMetaData.setContentType(file.getContentType());
                fileMetaData.setContentLength(file.getSize());

                amazonS3Client.putObject(bucket, fileName, inputStream, fileMetaData);
                resultBuilder.append("https://draw-map.s3.amazonaws.com/").append(fileName).append(",");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        if (resultBuilder.length() > 0) {
            resultBuilder.deleteCharAt(resultBuilder.length() - 1); // 마지막 쉼표 제거
        }

        String result = resultBuilder.toString();
        return result;
    }
}
