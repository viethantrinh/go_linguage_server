package tech.trvihnls.features.song.dtos.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({"id", "name", "audioUrl", "englishLyric", "vietnameseLyric", "wordTimeStamp"})
public class SongAfterUploadToCloudinaryResponse {
    private long id;
    private String name;
    private String audioUrl;
    private String englishLyric;
    private String vietnameseLyric;
    @Builder.Default
    private List<WordTimestamp> timestamps = new ArrayList<>();

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class WordTimestamp {
        private String word;
        private double startTime;
        private double endTime;
    }
}
