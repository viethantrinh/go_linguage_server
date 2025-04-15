//package tech.trvihnls.general;
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.*;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.web.client.RestTemplate;
//
//import java.util.Arrays;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import static org.junit.jupiter.api.Assertions.assertFalse;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//
//@Slf4j
//@SpringBootTest
//@ActiveProfiles({"dev"})
//public class GroqAITest {
//
//    private static final String GROQ_API_KEY = "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";
//    private static final String GROQ_API_PROMPT_URL = "https://api.groq.com/openai/v1/chat/completions";
//    private RestTemplate restTemplate = new RestTemplate();
//
//    @Test
//    void testSendPromptToGroqAPI() {
//        // Create request headers with API key
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        headers.set("Authorization", "Bearer " + GROQ_API_KEY);
//
//        // Create the messages list with user prompt
//        String userPrompt = """
//                Please create a simple and easy-to-understand English song with exactly 4 verses, each verse containing 4 lines. The song must include the following vocabulary words: "America", "Germany", "Japan", "English". Then, translate the song into Vietnamese, maintaining the same structure and meaning.
//                Format the result as follows:
//                - Only provide the English lyrics, without any explanation or introduction
//                - After the English lyrics, add a hyphen "-" on a separate line
//                - Next, provide the Vietnamese translation of the song
//                - No line breaks between verses, lines should be consecutive
//                - Do not add any information other than the requested content
//                """;
//
//        Map<String, Object> userMessage = Map.of(
//                "role", "user",
//                "content", userPrompt
//        );
//
//        // Create the request body
//        Map<String, Object> requestBody = new HashMap<>();
//        requestBody.put("model", "llama-3.3-70b-versatile");
//        requestBody.put("messages", List.of(userMessage));
//
//        // Create the HTTP entity with headers and body
//        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);
//
//        // Send the request and get the response
//        ResponseEntity<Map> responseEntity = restTemplate.exchange(
//                GROQ_API_PROMPT_URL,
//                HttpMethod.POST,
//                requestEntity,
//                Map.class
//        );
//
//        // Extract and log the response
//        Map<String, Object> responseBody = responseEntity.getBody();
////        log.info("Groq API Response: {}", responseBody);
//
//        // Assert that we received a valid response
//        assertNotNull(responseBody);
//
//        // Extract the generated text from the response
//        List<Map<String, Object>> choices = (List<Map<String, Object>>) responseBody.get("choices");
//        Map<String, Object> firstChoice = choices.get(0);
//        Map<String, Object> message = (Map<String, Object>) firstChoice.get("message");
//        String generatedContent = (String) message.get("content");
//
////        log.info("Generated content: {}", generatedContent);
//        assertNotNull(generatedContent);
//        assertFalse(generatedContent.isEmpty());
//
//        // Split the text into English and Vietnamese parts
//        String[] parts = generatedContent.split("-");
//        String englishPart = parts[0].trim();
//        String vietnamesePart = parts[1].trim();
//
//        // Process English part - remove empty lines but keep the structure of 4-line verses
//        englishPart = englishPart.replaceAll("\\n\\s*\\n+", "\n");
//
//        // Process Vietnamese part
//        vietnamesePart = vietnamesePart.replaceAll("\\n\\s*\\n+", "\n");
//
//        System.out.println(englishPart);
//        System.out.println(vietnamesePart);
//    }
//
//    @Test
//    void testPromptGetLyricFromGroq() {
//        String prompt = """
//                Please create a simple and easy-to-understand English song with exactly 4 verses, each verse containing 4 lines. The song must include the following vocabulary words: "America", "Germany", "Japan", "English". Then, translate the song into Vietnamese, maintaining the same structure and meaning.
//                Format the result as follows:
//                - Only provide the English lyrics, without any explanation or introduction
//                - After the English lyrics, add a hyphen "-" on a separate line
//                - Next, provide the Vietnamese translation of the song
//                - No line breaks between verses, lines should be consecutive
//                - Do not add any information other than the requested content
//                """;
//    }
//
//    @Test
//    void testGenerateDynamicPrompt() {
//        // Your array of vocabulary words
//        String[] vocabularyWords = {"sunshine", "mountain", "ocean", "happiness"};
//
//
//        // Convert the array to a comma-separated string with quotes
//        String vocabularyString = Arrays.stream(vocabularyWords)
//                .map(word -> "\"" + word + "\"")
//                .reduce((a, b) -> a + ", " + b)
//                .orElse("");
//
//        // Create the prompt with the dynamic vocabulary
//        String prompt = """
//                Please create a simple and easy-to-understand English song with exactly 4 verses, each verse containing 4 lines. The song must include the following vocabulary words: %s. Then, translate the song into Vietnamese, maintaining the same structure and meaning.
//                Format the result as follows:
//                - Only provide the English lyrics, without any explanation or introduction
//                - After the English lyrics, add a hyphen "-" on a separate line
//                - Next, provide the Vietnamese translation of the song
//                - No line breaks between verses, lines should be consecutive
//                - Do not add any information other than the requested content
//                """.formatted(vocabularyString);
//
//        // Print the result
//        System.out.println(prompt);
//    }
//
//    @Test
//    void testHandleGroqLyricPromptResult() {
//        String promptResult = """
//                I've traveled to America, seen the city lights
//                Met people from all over, it's a beautiful sight
//                I've heard they speak English, with a special flair
//                And the music's got rhythm, beyond compare
//
//                I've walked through Germany, with its castles so tall
//                Learned about its history, and had a ball
//                The beer and the food, are a tasty delight
//                And the people's so friendly, day and night
//
//                I've visited Japan, with its culture so bright
//                Saw the cherry blossoms, in the morning light
//                The food and the temples, are a wondrous sight
//                And the people's so polite, with a gentle might
//
//                I'll travel the world, and learn every day
//                In English, I'll communicate, in every way
//                From America to Japan, to Germany too
//                I'll make friends and memories, that's what I'll do
//
//                -
//                Tôi đã đến Mỹ, nhìn thấy ánh đèn thành phố
//                Gặp gỡ mọi người từ khắp nơi, thật là một cảnh tượng đẹp
//                Tôi đã nghe họ nói tiếng Anh, với một phong cách đặc biệt
//                Và âm nhạc có nhịp điệu, vượt trội
//
//                Tôi đã đi qua Đức, với những lâu đài cao vút
//                Tìm hiểu về lịch sử, và có một khoảng thời gian tuyệt vời
//                Bia và thức ăn, là một niềm vui ẩm thực
//                Và mọi người rất thân thiện, ngày và đêm
//
//                Tôi đã đến thăm Nhật Bản, với nền văn hóa tươi sáng
//                Nhìn thấy hoa anh đào, trong ánh sáng buổi sáng
//                Thức ăn và những ngôi đền, là một cảnh tượng kỳ diệu
//                Và mọi người rất lịch sự, với một sức mạnh nhẹ nhàng
//
//                Tôi sẽ đi du lịch khắp thế giới, và học hỏi mỗi ngày
//                Bằng tiếng Anh, tôi sẽ giao tiếp, theo mọi cách
//                Từ Mỹ đến Nhật Bản, đến Đức nữa
//                Tôi sẽ kết bạn và tạo ra những ký ức, đó là những gì tôi sẽ làm
//                """;
//
//        // Split the text into English and Vietnamese parts
//        String[] parts = promptResult.split("-");
//        String englishPart = parts[0].trim();
//        String vietnamesePart = parts[1].trim();
//
//        // Process English part - remove empty lines but keep the structure of 4-line verses
//        englishPart = englishPart.replaceAll("\\n\\s*\\n+", "\n");
//
//        // Process Vietnamese part
//        vietnamesePart = vietnamesePart.replaceAll("\\n\\s*\\n+", "\n");
//
//        // Combine the processed parts
//        String result = englishPart + "\n-\n" + vietnamesePart;
//
//        System.out.println(result);
//    }
//}
