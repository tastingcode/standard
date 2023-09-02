package com.shop.service.etc;

import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
@Slf4j
public class FileService {

    public String uploadFile(String uploadPath, MultipartFile multipartFile) throws IOException {
        String originalFilename = multipartFile.getOriginalFilename();
        String savedFileName = createSavedFileName(originalFilename);
        multipartFile.transferTo(new File(uploadPath + savedFileName));
        return savedFileName;
    }

    private String createSavedFileName(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        String ext = originalFilename.substring(pos + 1);
        return UUID.randomUUID().toString() + "." + ext;
    }

    public void deleteFile(String filePath){
        File deleteFile = new File(filePath);

        if (deleteFile.exists()){
            deleteFile.delete();
            log.info("파일을 삭제하였습니다.");
        } else
            log.info("파일이 존재하지 않습니다.");
    }

}
