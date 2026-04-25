package com.example.discordia.service.Cloudinary;


import com.cloudinary.Cloudinary;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


@Service
public class CloudinaryServiceImpl implements CloudinaryService {

    @Resource
    private Cloudinary cloudinary;

    // converts image into url
    @Override
    public String uploadFile(MultipartFile file, String folderName){

        try{
            HashMap<Object, Object> options = new HashMap<>();

            options.put("folder", folderName);
            Map uploadedFile = cloudinary.uploader().upload(
                    file.getBytes(), options
            );

            return (String) uploadedFile.get("secure_url");
        }  catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
