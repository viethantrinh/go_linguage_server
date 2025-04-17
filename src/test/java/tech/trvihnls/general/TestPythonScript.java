//package tech.trvihnls.general;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import lombok.extern.slf4j.Slf4j;
//import org.junit.jupiter.api.Test;
//import org.springframework.core.io.FileSystemResource;
//import org.springframework.http.HttpEntity;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.util.LinkedMultiValueMap;
//import org.springframework.util.MultiValueMap;
//import org.springframework.web.client.RestTemplate;
//
//import java.io.File;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//
//@Slf4j
//@ActiveProfiles({"test"})
//public class TestPythonScript {
//    private final RestTemplate restTemplate = new RestTemplate();
//    private final ObjectMapper objectMapper = new ObjectMapper();
//
//    private String apiUrl = "http://localhost:5000/align";
//
//    @Test
//    void testForceAlignment() throws IOException {
//        String lyrics = """
//                In the park, a woman walks,
//                With a smile, she softly talks.
//                A man sits under a tree,
//                Reading a book, calm and free.
//
//                A girl and a boy play in the sun,
//                Laughing together, having fun.
//                They chase each other, run and hide,
//                Joy and laughter, side by side.
//
//                The sun is bright, the sky is blue,
//                A perfect day for me and you.
//                The woman waves, the man stands tall,
//                Together they enjoy it all.
//
//                A girl and a boy play in the sun,
//                Laughing together, having fun.
//                They chase each other, run and hide,
//                Joy and laughter, side by side.
//
//                As the day ends, they wave goodbye,
//                Under the stars, in the night sky.
//                A woman, a man, a girl, a boy,
//                In the park, they found their joy.
//                """;
//
//        String audioPath = "./force_alignment/audio.mp3";
//
//        AlignmentResult alignmentResult = processAudioWithLyrics(audioPath, lyrics);
//        log.info("The final result: {}", objectMapper.writerWithDefaultPrettyPrinter()
//                .writeValueAsString(alignmentResult));
//    }
//
//    /**
//     * Process audio file and lyrics to get word-level timestamps
//     *
//     * @param audioFilePath Path to the audio file
//     * @param lyrics        Lyrics text content
//     * @return AlignmentResult containing word-level timestamps
//     * @throws IOException If any operation fails
//     */
//    public AlignmentResult processAudioWithLyrics(String audioFilePath, String lyrics)
//            throws IOException {
//
//        log.info("Processing audio file {} with lyrics", audioFilePath);
//
//        try {
//            // Prepare the multipart request
//            HttpHeaders headers = new HttpHeaders();
//            headers.setContentType(MediaType.MULTIPART_FORM_DATA);
//
//            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
//            body.add("audio_file", new FileSystemResource(new File(audioFilePath)));
//            body.add("lyrics", lyrics);
//
//            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
//
//            // Send the request to the Python API
//            ResponseEntity<String> response = restTemplate.postForEntity(apiUrl, requestEntity, String.class);
//
//            if (!response.getStatusCode().is2xxSuccessful()) {
//                throw new IOException("API call failed with status: " + response.getStatusCode());
//            }
//
//            // Parse the JSON response
//            JsonResponse jsonResponse = objectMapper.readValue(response.getBody(), JsonResponse.class);
//
//            // Convert to our result object
//            AlignmentResult result = new AlignmentResult();
//            result.setWords(jsonResponse.getWords());
//
//            log.info("Successfully processed alignment with {} words", result.getWords().size());
//            return result;
//
//        } catch (Exception e) {
//            log.error("Error processing audio alignment", e);
//            throw new IOException("Failed to process audio alignment: " + e.getMessage(), e);
//        }
//    }
//
//    // Model classes for JSON parsing and result
//
//    public static class JsonResponse {
//        private List<WordTimestamp> words;
//
//        public List<WordTimestamp> getWords() {
//            return words;
//        }
//
//        public void setWords(List<WordTimestamp> words) {
//            this.words = words;
//        }
//    }
//
//    public static class AlignmentResult {
//        private List<WordTimestamp> words = new ArrayList<>();
//
//        public List<WordTimestamp> getWords() {
//            return words;
//        }
//
//        public void setWords(List<WordTimestamp> words) {
//            this.words = words;
//        }
//    }
//
//    public static class WordTimestamp {
//        private String word;
//        private double start;
//        private double end;
//
//        public String getWord() {
//            return word;
//        }
//
//        public void setWord(String word) {
//            this.word = word;
//        }
//
//        public double getStart() {
//            return start;
//        }
//
//        public void setStart(double start) {
//            this.start = start;
//        }
//
//        public double getEnd() {
//            return end;
//        }
//
//        public void setEnd(double end) {
//            this.end = end;
//        }
//    }
//}
