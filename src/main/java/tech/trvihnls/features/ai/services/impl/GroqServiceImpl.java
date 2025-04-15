package tech.trvihnls.features.ai.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import tech.trvihnls.commons.utils.AppConstants;
import tech.trvihnls.features.ai.dtos.response.GroqWhisperResponse;
import tech.trvihnls.features.ai.services.GroqService;

import java.io.IOException;
import java.time.Duration;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GroqServiceImpl implements GroqService {

    private final WebClient webClient;
    private final RestTemplate restTemplate;

    private static final String GROQ_TEST_API_KEY = "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";
    private static final String GROQ_API_PROMPT_URL = "https://api.groq.com/openai/v1/chat/completions";

    @Value("${groq.api-key}")
    private String apiKey;

    @Autowired
    public GroqServiceImpl(WebClient.Builder builder, RestTemplate restTemplate) {
        this.webClient = builder.baseUrl(AppConstants.GROQ_TRANSCRIBE_URL).build();
        this.restTemplate = restTemplate;
    }

    @Override
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

    @SuppressWarnings("all")
    @Override
    public Map<String, String> createEnglishAndVietnameseSong(List<String> words) {
        // Tạo prompt từ danh sách từ
        String[] vocabularyWords = words.toArray(new String[0]);

        // Convert the array to a comma-separated string with quotes
        String vocabularyString = Arrays.stream(vocabularyWords)
                .map(word -> "\"" + word + "\"")
                .reduce((a, b) -> a + ", " + b)
                .orElse("");

        // Create the prompt with the dynamic vocabulary
        String prompt = """
                Please create a simple and easy-to-understand English song with exactly 4 verses, each verse containing 4 lines. The song must include the following vocabulary words: %s. Then, translate the song into Vietnamese, maintaining the same structure and meaning.
                Format the result as follows:
                - Only provide the English lyrics, without any explanation or introduction
                - After the English lyrics, add a hyphen "-" on a separate line
                - Next, provide the Vietnamese translation of the song
                - No line breaks between verses, lines should be consecutive
                - Do not add any information other than the requested content
                """.formatted(vocabularyString);

        // Create request headers with API key
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + GROQ_TEST_API_KEY);

        Map<String, Object> userMessage = Map.of(
                "role", "user",
                "content", prompt
        );

        // Create the request body
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "llama-3.3-70b-versatile");
        requestBody.put("messages", List.of(userMessage));

        // Create the HTTP entity with headers and body
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        // Send the request and get the response
        ResponseEntity<Map> responseEntity = restTemplate.exchange(
                GROQ_API_PROMPT_URL,
                HttpMethod.POST,
                requestEntity,
                Map.class
        );

        // Extract and log the response
        Map<String, Object> responseBody = responseEntity.getBody();

        // Extract the generated text from the response
        List<Map<String, Object>> choices = (List<Map<String, Object>>) responseBody.get("choices");
        Map<String, Object> firstChoice = choices.get(0);
        Map<String, Object> message = (Map<String, Object>) firstChoice.get("message");
        String generatedContent = (String) message.get("content");

        // Split the text into English and Vietnamese parts
        String[] parts = generatedContent.split("-");
        String englishPart = parts[0].trim();
        String vietnamesePart = parts[1].trim();

        // Process English part - remove empty lines but keep the structure of 4-line verses
        englishPart = englishPart.replaceAll("\\n\\s*\\n+", "\n");

        // Process Vietnamese part
        vietnamesePart = vietnamesePart.replaceAll("\\n\\s*\\n+", "\n");

        Map<String, String> result = new HashMap<>();
        result.put("englishLyric", englishPart);
        result.put("vietnameseLyric", vietnamesePart);
        return result;
    }
}
