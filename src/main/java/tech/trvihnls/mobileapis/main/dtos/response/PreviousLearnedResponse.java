package tech.trvihnls.mobileapis.main.dtos.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({"vocabularies", "dialogues"})
public class PreviousLearnedResponse {
    @Builder.Default
    private List<TopicReviewResponse<Object>> vocabularies = new ArrayList<>();

    @Builder.Default
    private List<TopicReviewResponse<Object>> dialogues = new ArrayList<>();
}
