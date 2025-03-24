package tech.trvihnls.mobileapis.ai.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import tech.trvihnls.commons.utils.AppConstants;
import tech.trvihnls.mobileapis.ai.dtos.response.GroqWhisperResponse;

import java.io.IOException;
import java.time.Duration;

@Service
public class GroqTranscribeService {
    private final WebClient webClient;

    @Value("${groq.api-key}")
    private String apiKey;

    public GroqTranscribeService(WebClient.Builder builder) {
        this.webClient = builder.baseUrl(AppConstants.GROQ_TRANSCRIBE_URL).build();
    }

    public String transcribeAudio(MultipartFile audioFile) {
        try {
            // Chuẩn bị file audio
            byte[] audioBytes = audioFile.getBytes();
            String filename = audioFile.getOriginalFilename();

            // Chuẩn bị multipart form data
            MultiValueMap<String, Object> parts = new LinkedMultiValueMap<>();

            // Thêm file
            ByteArrayResource resource = new ByteArrayResource(audioBytes) {
                @Override
                public String getFilename() {
                    return filename != null ? filename : "audio.ogg";
                }
            };

            parts.add("file", resource);

            // Thêm model và các tham số khác
            parts.add("model", "whisper-large-v3");

            // Gọi Groq Whisper API
            GroqWhisperResponse response = webClient.post()
                    .headers(headers -> headers.setBearerAuth(apiKey))
                    .contentType(MediaType.MULTIPART_FORM_DATA)
                    .body(BodyInserters.fromMultipartData(parts))
                    .retrieve()
                    .bodyToMono(GroqWhisperResponse.class)
                    .timeout(Duration.ofSeconds(60)) // Timeout sau 60 giây
                    .block();

            if (response != null && response.getText() != null) {
                return response.getText().trim();
            } else {
                throw new RuntimeException("Không nhận được kết quả từ Groq Whisper API");
            }
        } catch (IOException e) {
            throw new RuntimeException("Lỗi khi xử lý file âm thanh", e);
        }
    }
}
