package tech.trvihnls.features.ai.services.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import tech.trvihnls.commons.domains.Song;
import tech.trvihnls.commons.repositories.SongRepository;
import tech.trvihnls.commons.utils.enums.SongCreationStatusEnum;
import tech.trvihnls.features.ai.dtos.response.MusicGenerationStatusResponse;
import tech.trvihnls.features.ai.services.SunoService;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class SunoServiceImpl implements SunoService {
    // Replace with your actual values
    private static final String API_BASE_URL = "https://apibox.erweima.ai"; // Or your deployed Suno API endpoint
    private static final String API_KEY = "12c23d30edb9437132615ded67425f10"; // Replace with your actual API key
    private final RestTemplate restTemplate;
    private final HttpHeaders headers;
    private final SongRepository songRepository;

    public SunoServiceImpl(SongRepository songRepository) {
        this.restTemplate = new RestTemplate();
        this.headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + API_KEY);
        this.songRepository = songRepository;
    }

    @Override
    public String generateMusic(Song song) {
        log.info("Generating music for song: {}", song.getName());

        String title = song.getName();
        String englishLyric = song.getEnglishLyric();

        // Create request payload
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("prompt", englishLyric);
        requestBody.put("style", "fun, for learning vocabulary");
        requestBody.put("title", title);
        requestBody.put("customMode", true);
        requestBody.put("instrumental", false);
        requestBody.put("model", "V4");
        requestBody.put("callBackUrl", "https://api.example.com/callback");

        // Create HTTP entity with headers and body
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        try {
            // Make the synchronous POST request
            ResponseEntity<Map> response = restTemplate.exchange(
                    API_BASE_URL + "/api/v1/generate",
                    HttpMethod.POST,
                    requestEntity,
                    Map.class
            );

            Map<String, Object> body = response.getBody();
            if (body != null && body.containsKey("data")) {
                Map<String, Object> data = (Map<String, Object>) body.get("data");
                String taskId = (String) data.get("taskId");
                log.info("Successfully generated music with taskId: {}", taskId);
                return taskId;
            } else {
                log.error("Failed to extract taskId from response: {}", body);
                throw new RuntimeException("Failed to extract taskId from response");
            }
        } catch (Exception e) {
            log.error("Error generating music: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to generate music", e);
        }
    }

    @Override
    public boolean isSongCreateSuccess(String taskId) {
        try {
            // Build URL with query parameters
            String url = UriComponentsBuilder.fromHttpUrl(API_BASE_URL + "/api/v1/generate/record-info")
                    .queryParam("taskId", taskId)
                    .toUriString();

            // Create HTTP entity with headers
            HttpEntity<?> requestEntity = new HttpEntity<>(headers);

            // Make the GET request
            ResponseEntity<MusicGenerationStatusResponse> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    requestEntity,
                    MusicGenerationStatusResponse.class
            );

            MusicGenerationStatusResponse statusResponse = response.getBody();
            if (statusResponse == null) {
                throw new RuntimeException("Empty response body received");
            }

            // Log the status information
            if (statusResponse.getData() != null) {
                log.info("Generation status: {}", statusResponse.getData().getStatus());

                // Log error details if present
                if (statusResponse.getData().getErrorCode() != null) {
                    log.error("Generation error: {} ({})",
                            statusResponse.getData().getErrorMessage(),
                            statusResponse.getData().getErrorCode());
                }

                if (statusResponse.getData().getStatus().equals("FIRST_SUCCESS") || statusResponse.getData().getStatus().equals("SUCCESS")) {
                    Song songBySunoTaskId = songRepository.findBySunoTaskId(statusResponse.getData().getTaskId());
                    songBySunoTaskId.setCreationStatus(SongCreationStatusEnum.audio_processed);
                    songBySunoTaskId.setAudioUrl(statusResponse.getData().getResponse().getSunoData().get(0).getAudioUrl());
                    songRepository.save(songBySunoTaskId);
                    return true;
                }
            }
        } catch (Exception e) {
            log.error("Failed to check generation status: {}", e.getMessage(), e);
            throw new RuntimeException("Status check request failed", e);
        }
        return false;
    }
}
