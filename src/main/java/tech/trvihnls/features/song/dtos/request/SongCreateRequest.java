package tech.trvihnls.features.song.dtos.request;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SongCreateRequest {
    private String name;

    @Builder.Default
    private List<String> words = new ArrayList<>();
}
