package tech.trvihnls.general;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;
import com.cloudinary.utils.StringUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@SpringBootTest
public class CloudinaryTest {

    @Autowired
    private Cloudinary cloudinary;

    @Test
    void testCloudinaryStringUtils() {
        System.out.println(StringUtils.join(List.of("ngu", "nguoi", "_", "!"), ""));
    }

    @Test
    void testUploadAudioWithCustomOption() throws IOException {
        Map result = cloudinary.uploader().upload("https://res.cloudinary.com/golinguage/video/upload/v1741530722/audios/b6e03d1f-cad0-4267-87a0-b5d398e3d51a.ogg",
                ObjectUtils.asMap(
                        "public_id", UUID.randomUUID().toString(),
                        "resource_type", "video",
                        "asset_folder", "audios",
                        "transformation", new Transformation()
                                .audioCodec("opus")
                                .audioFrequency("48000")
                                .fetchFormat("ogg")
                                .quality("auto")
                )
        );
        System.out.println(new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(result));
    }

    @Test
    void testUploadImageWithCustomOption() throws IOException {
        // Option 1: Using ClassPathResource (works in development environment)
        Resource resource = new ClassPathResource("images/topics");
        File[] files = null;
        try {
            File directory = resource.getFile();
            files = directory.listFiles();
        } catch (FileNotFoundException e) {
            System.out.println("Directory not found: " + e.getMessage());
        }

        // 128 x 128 for very small image (in lesson vocab)
        // 256 x 256 for thumbnail (topics list)
        // 512 x 512 for card (bigger for vocabulary card)

        for (File file : files) {
            Map result = cloudinary.uploader().upload(file,
                    ObjectUtils.asMap(
                            "public_id", UUID.randomUUID().toString(),
                            "resource_type", "image",
                            "asset_folder", "images",
                            "transformation", new Transformation()
                                    .width(256)
                                    .height(256)
                                    .crop("fill")
                                    .fetchFormat("webp")
                                    .quality("auto")
                    )
            );
            System.out.println(new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(result));
        }
    }
}
