package com.umc.drawmap.controller;


import com.umc.drawmap.service.S3FileService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class testController {

    S3FileService s3FileService;
    public testController(S3FileService s3FileService) {
        this.s3FileService = s3FileService;
    }

    @PostMapping
    String fileUpload(@RequestPart("file") MultipartFile multipartFile) {
        return s3FileService.upload(multipartFile);
    }
}
