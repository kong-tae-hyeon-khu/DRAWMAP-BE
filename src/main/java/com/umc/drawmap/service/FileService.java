package com.umc.drawmap.service;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class FileService {


    public static String fileUpload(List<MultipartFile> files) throws IOException{

        String rootPath = System.getProperty("user.dir");
        String fileDir = rootPath + "/files/";
        List<String> list = new ArrayList<>();
        for(MultipartFile file: files){
            File saveFile = new File(fileDir, file.getOriginalFilename());
            file.transferTo(saveFile);
            list.add(saveFile.getPath());
        }
        return String.join(",",list);

    }
}