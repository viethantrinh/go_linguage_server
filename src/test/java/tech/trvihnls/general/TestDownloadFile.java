package tech.trvihnls.general;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@ActiveProfiles({"test"})
public class TestDownloadFile {




    @Test
    void testDownloadFileFromUrl() throws IOException {
        String audioUrl = "https://file-examples.com/storage/fe0d4ef3b467fe96a99bd97/2017/11/file_example_MP3_700KB.mp3";
        String outputPath = "downloaded_audio.mp3";

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

//            Files.delete(Path.of(outputPath));

            System.out.println("Audio file downloaded successfully to: " + response.body());

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

}
