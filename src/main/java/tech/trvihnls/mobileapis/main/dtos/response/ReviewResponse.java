package tech.trvihnls.mobileapis.main.dtos.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({"exams", "flashCards", "previousLearned"})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReviewResponse {
    @Builder.Default
    private List<Object> exams = new ArrayList<>();

    @Builder.Default
    private List<TopicReviewResponse<Object>> flashCards = new ArrayList<>();

    private PreviousLearnedResponse previousLearned;
}



