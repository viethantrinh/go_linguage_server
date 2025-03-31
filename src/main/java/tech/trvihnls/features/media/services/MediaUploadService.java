package tech.trvihnls.features.media.services;

import org.springframework.web.multipart.MultipartFile;
import tech.trvihnls.features.media.dtos.response.CloudinaryUrlResponse;

public interface MediaUploadService {
    CloudinaryUrlResponse uploadImage(MultipartFile multipartFile, int width, int height);

    CloudinaryUrlResponse uploadAudio(MultipartFile multipartFile);
}

