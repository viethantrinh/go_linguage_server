package tech.trvihnls.features.ai.services.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import tech.trvihnls.commons.domains.Song;
import tech.trvihnls.commons.domains.WordTime;
import tech.trvihnls.commons.repositories.SongRepository;
import tech.trvihnls.commons.utils.enums.SongCreationStatusEnum;
import tech.trvihnls.features.ai.services.WhisperAlignmentService;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class WhisperAlignmentServiceImpl implements WhisperAlignmentService {
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final SongRepository songRepository;


    @Override
    public Song forceAlignmentSong(Song song) {
        String audioUrl = song.getAudioUrl();
        String lyrics = song.getEnglishLyric();
        String audioPath = "audio.mp3";
        downloadAudioUrlAndGetPath(audioUrl, audioPath);
        try {
            AlignmentResult alignmentResult = processAudioWithLyrics(audioPath, lyrics);
            List<WordTimestamp> wtsList = alignmentResult.getWords();
            tech.trvihnls.commons.domains.WordTimestamp songWordTimeStamp = tech.trvihnls.commons.domains.WordTimestamp.builder()
                    .text(song.getEnglishLyric())
                    .words(
                            wtsList.stream()
                                    .map(wt -> WordTime.builder()
                                            .word(wt.getWord())
                                            .start(wt.getStart())
                                            .end(wt.getEnd())
                                            .build())
                                    .toList()
                    )
                    .build();
            song.setWordTimeStamp(songWordTimeStamp);
            song.setCreationStatus(SongCreationStatusEnum.alignment_processed);
            Song savedSong = songRepository.save(song);
            Files.delete(Path.of(audioPath));
            return savedSong;
        } catch (IOException e) {
            log.error("Error during force alignment: {}", e.getMessage());
            throw new RuntimeException("Failed to process audio alignment: " + e.getMessage(), e);
        }
    }

    private void downloadAudioUrlAndGetPath(String audioUrl, String outputPath) {
        try {
            // Create HttpClient
            HttpClient client = HttpClient.newHttpClient();

            // Create request
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(audioUrl))
                    .GET()
                    .build();

            // Send request and save response body to file
            HttpResponse<Path> response = client.send(request,
                    HttpResponse.BodyHandlers.ofFile(Paths.get(outputPath)));

            System.out.println("Audio file downloaded successfully to: " + response.body());

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private AlignmentResult processAudioWithLyrics(String audioFilePath, String lyrics)
            throws IOException {

        log.info("Processing audio file {} with lyrics", audioFilePath);

        try {
            // Prepare the multipart request
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("audio_file", new FileSystemResource(new File(audioFilePath)));
            body.add("lyrics", lyrics);

            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

            // Send the request to the Python API
            ResponseEntity<String> response = restTemplate.postForEntity("http://localhost:5000/align", requestEntity, String.class);

            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new IOException("API call failed with status: " + response.getStatusCode());
            }

            // Parse the JSON response
            JsonResponse jsonResponse = objectMapper.readValue(response.getBody(), JsonResponse.class);

            // Convert to our result object
            AlignmentResult result = new AlignmentResult();
            result.setWords(jsonResponse.getWords());

            log.info("Successfully processed alignment with {} words", result.getWords().size());
            return result;

        } catch (Exception e) {
            log.error("Error processing audio alignment", e);
            throw new IOException("Failed to process audio alignment: " + e.getMessage(), e);
        }
    }

    // Model classes for JSON parsing and result
    public static class JsonResponse {
        private List<WordTimestamp> words;

        public List<WordTimestamp> getWords() {
            return words;
        }

        public void setWords(List<WordTimestamp> words) {
            this.words = words;
        }
    }

    public static class AlignmentResult {
        private List<WordTimestamp> words = new ArrayList<>();

        public List<WordTimestamp> getWords() {
            return words;
        }

        public void setWords(List<WordTimestamp> words) {
            this.words = words;
        }
    }

    public static class WordTimestamp {
        private String word;
        private double start;
        private double end;

        public String getWord() {
            return word;
        }

        public void setWord(String word) {
            this.word = word;
        }

        public double getStart() {
            return start;
        }

        public void setStart(double start) {
            this.start = start;
        }

        public double getEnd() {
            return end;
        }

        public void setEnd(double end) {
            this.end = end;
        }
    }
}
