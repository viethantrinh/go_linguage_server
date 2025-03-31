package tech.trvihnls.features.main.dtos.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({"id", "name", "displayOrder", "data"})
public class TopicReviewResponse<T> {
    private long id;
    private String name;
    private int displayOrder;

    @Builder.Default
    private List<T> data = new ArrayList<>();
}
