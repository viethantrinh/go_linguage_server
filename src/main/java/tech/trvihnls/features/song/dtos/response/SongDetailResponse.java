package tech.trvihnls.features.song.dtos.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;
import tech.trvihnls.commons.domains.WordTimestamp;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({"id", "name", "audioUrl", "englishLyric", "vietnameseLyric", "displayOrder", "wordTimeStamp"})
public class SongDetailResponse {
    private long id;
    private String name;
    private String audioUrl;
    private String englishLyric;
    private String vietnameseLyric;
    private int displayOrder;
    private WordTimestamp wordTimestamp;
}
