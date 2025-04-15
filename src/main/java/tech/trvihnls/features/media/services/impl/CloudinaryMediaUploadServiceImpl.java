package tech.trvihnls.features.media.services.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tech.trvihnls.commons.exceptions.AppException;
import tech.trvihnls.commons.utils.AppConstants;
import tech.trvihnls.commons.utils.enums.ErrorCodeEnum;
import tech.trvihnls.features.media.dtos.response.CloudinaryUrlResponse;
import tech.trvihnls.features.media.services.MediaUploadService;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class CloudinaryMediaUploadServiceImpl implements MediaUploadService {

    private final Cloudinary cloudinary;

    @Override
    public CloudinaryUrlResponse uploadImage(MultipartFile multipartFile, int width, int height) {
        assert multipartFile.getOriginalFilename() != null;
        var cloudinaryUploadOptions = ObjectUtils.asMap(
                AppConstants.CLOUDINARY_PUBLIC_ID_KEY, UUID.randomUUID().toString(),
                AppConstants.CLOUDINARY_ASSET_FOLDER_KEY, "images",
                AppConstants.CLOUDINARY_RESOURCE_TYPE_KEY, "image",
                AppConstants.CLOUDINARY_TRANSFORMATION_KEY, new Transformation<>()
                        .width(width)
                        .height(height)
                        .crop("fill")
                        .fetchFormat("webp")
                        .quality("auto")
        );

        Map result;
        try {
            result = cloudinary.uploader().upload(multipartFile, cloudinaryUploadOptions);
        } catch (IOException e) {
            throw new AppException(ErrorCodeEnum.UPLOAD_RESOURCE_FAILED);
        }


        return CloudinaryUrlResponse.builder()
                .secureUrl((String) result.get("secure_url"))
                .url((String) result.get("url"))
                .build();
    }

    @Override
    public CloudinaryUrlResponse uploadAudio(MultipartFile multipartFile) {
        assert multipartFile.getOriginalFilename() != null;
        var cloudinaryUploadOptions = ObjectUtils.asMap(
                AppConstants.CLOUDINARY_PUBLIC_ID_KEY, UUID.randomUUID().toString(),
                AppConstants.CLOUDINARY_ASSET_FOLDER_KEY, "audios",
                AppConstants.CLOUDINARY_RESOURCE_TYPE_KEY, "video",
                AppConstants.CLOUDINARY_TRANSFORMATION_KEY, new Transformation<>()
                        .audioCodec("opus")
                        .audioFrequency("48000")
                        .fetchFormat("ogg")
                        .quality("auto")
        );

        Map result;
        try {
            result = cloudinary.uploader().upload(multipartFile, cloudinaryUploadOptions);
        } catch (IOException e) {
            throw new AppException(ErrorCodeEnum.UPLOAD_RESOURCE_FAILED);
        }
        return CloudinaryUrlResponse.builder()
                .secureUrl((String) result.get("secure_url"))
                .url((String) result.get("url"))
                .build();
    }

    @Override
    public CloudinaryUrlResponse uploadAudio(String audioUrl) {
        assert audioUrl != null;
        var cloudinaryUploadOptions = ObjectUtils.asMap(
                AppConstants.CLOUDINARY_PUBLIC_ID_KEY, UUID.randomUUID().toString(),
                AppConstants.CLOUDINARY_ASSET_FOLDER_KEY, "audios",
                AppConstants.CLOUDINARY_RESOURCE_TYPE_KEY, "video",
                AppConstants.CLOUDINARY_TRANSFORMATION_KEY, new Transformation<>()
                        .audioCodec("opus")
                        .audioFrequency("48000")
                        .fetchFormat("ogg")
                        .quality("auto")
        );

        Map result;
        try {
            result = cloudinary.uploader().upload(audioUrl, cloudinaryUploadOptions);
        } catch (IOException e) {
            throw new AppException(ErrorCodeEnum.UPLOAD_RESOURCE_FAILED);
        }
        return CloudinaryUrlResponse.builder()
                .secureUrl((String) result.get("secure_url"))
                .url((String) result.get("url"))
                .build();
    }

    @Override
    public CloudinaryUrlResponse uploadAudio(byte[] audioByte) {
        assert audioByte != null;
        var cloudinaryUploadOptions = ObjectUtils.asMap(
                AppConstants.CLOUDINARY_PUBLIC_ID_KEY, UUID.randomUUID().toString(),
                AppConstants.CLOUDINARY_RESOURCE_TYPE_KEY, "video",
                AppConstants.CLOUDINARY_ASSET_FOLDER_KEY, "audios",
                AppConstants.CLOUDINARY_TRANSFORMATION_KEY, new Transformation<>()
                        .audioCodec("opus")
                        .audioFrequency("48000")
                        .fetchFormat("ogg")
                        .quality("auto")
        );

        Map result;
        try {
            result = cloudinary.uploader().upload(audioByte, cloudinaryUploadOptions);
        } catch (IOException e) {
            throw new AppException(ErrorCodeEnum.UPLOAD_RESOURCE_FAILED);
        }
        return CloudinaryUrlResponse.builder()
                .secureUrl((String) result.get("secure_url"))
                .url((String) result.get("url"))
                .build();
    }
}
