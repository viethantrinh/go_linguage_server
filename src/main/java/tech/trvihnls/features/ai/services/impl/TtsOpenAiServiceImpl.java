package tech.trvihnls.features.ai.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import tech.trvihnls.commons.repositories.SentenceRepository;
import tech.trvihnls.commons.repositories.WordRepository;
import tech.trvihnls.features.ai.services.TtsService;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class TtsOpenAiServiceImpl implements TtsService {

    private final RestTemplate restTemplate;
    private final WordRepository wordRepository;
    private final SentenceRepository sentenceRepository;

    @Value("${tts.elevenlab.api-key}")
    private String apiKey;

    // Default values as per your requirements
    private static final String DEFAULT_VOICE_ID = "XfNU2rGpBa01ckF309OY";
    private static final String DEFAULT_MODEL = "eleven_turbo_v2";
    private static final String DEFAULT_OUTPUT_FORMAT = "opus_48000_32";
    private static final String API_URL = "https://api.elevenlabs.io/v1/text-to-speech/";

    /**
     * Converts text to speech using default settings
     *
     * @param text The text to convert to speech
     * @return byte array containing the audio data
     */
    @Override
    public byte[] requestTextToSpeech(String text) {
        // Create request body
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("text", text);
        requestBody.put("model_id", DEFAULT_MODEL);

        // Create headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("xi-api-key", apiKey);

        // Create request entity
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        // Build the URL with voice ID and output format
        String url = API_URL + DEFAULT_VOICE_ID + "?output_format=" + DEFAULT_OUTPUT_FORMAT;

        // Make the API call
        ResponseEntity<byte[]> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                requestEntity,
                byte[].class
        );

        return response.getBody();
    }
}
