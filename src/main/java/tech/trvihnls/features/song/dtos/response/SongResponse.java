package tech.trvihnls.features.song.dtos.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({"id", "name", "displayOrder"})
public class SongResponse {
    private long id;
    private String name;
    private int displayOrder;
}
