package tech.trvihnls.features.song.dtos.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;
import tech.trvihnls.commons.utils.enums.SongCreationStatusEnum;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({"id", "name", "displayOrder"})
public class SongResponse {
    private long id;
    private String name;
    private String englishLyric;
    private String vietnameseLyric;
    private String audioUrl;
    private String sunoTaskId;
    private SongCreationStatusEnum creationStatus;
    private int displayOrder;
    private LocalDateTime createdAt;

}
