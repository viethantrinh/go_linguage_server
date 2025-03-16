package tech.trvihnls.mobileapis.media.services;

import org.springframework.web.multipart.MultipartFile;
import tech.trvihnls.mobileapis.media.dtos.response.CloudinaryUrlResponse;

public interface MediaUploadService {
    CloudinaryUrlResponse uploadImage(MultipartFile multipartFile, int width, int height);

    CloudinaryUrlResponse uploadAudio(MultipartFile multipartFile);
}

