package tech.trvihnls.features.song.dtos.response;

import lombok.*;
import tech.trvihnls.commons.utils.enums.SongCreationStatusEnum;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SongCreateResponse {
    private long id;
    private String name;
    private String englishLyric;
    private String vietnameseLyric;
    private String sunoTaskId;
    private SongCreationStatusEnum creationStatus;
}
