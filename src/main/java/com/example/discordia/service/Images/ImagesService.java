package com.example.discordia.service.Images;


import com.example.discordia.dto.UploadImageDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface ImagesService {

    UploadImageDto uploadMessageImage(UUID messageId, UUID messageTypeId, String messageType, MultipartFile image);
}
