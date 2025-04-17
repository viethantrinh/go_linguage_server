package tech.trvihnls.features.song.dtos.request;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SunoCreateSongRequest {
    private long songId;
    private String lyric;
}
