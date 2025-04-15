package tech.trvihnls.features.song.dtos.response;

import lombok.*;

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
}
