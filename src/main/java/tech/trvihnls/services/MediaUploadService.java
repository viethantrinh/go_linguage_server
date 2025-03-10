package tech.trvihnls.services;

import org.springframework.web.multipart.MultipartFile;
import tech.trvihnls.models.dtos.media.CloudinaryUrlResponse;

public interface MediaUploadService {
    CloudinaryUrlResponse uploadImage(MultipartFile multipartFile, int width, int height);

    CloudinaryUrlResponse uploadAudio(MultipartFile multipartFile);
}

