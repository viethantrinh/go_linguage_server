package tech.trvihnls.features.excercise.dtos.response.admin;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({"id", "optionType", "isCorrect", "wordId", "sentenceId"})
public class MultipleChoiceOptionDetailResponse {
    private long id;

    private String optionType;

    @JsonProperty("isCorrect")
    private boolean isCorrect;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long wordId;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private WordDetail word;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long sentenceId;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private SentenceDetail sentence;

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class WordDetail {
        private long wordId;
        private String englishText;
        private String vietnameseText;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SentenceDetail {
        private long sentenceId;
        private String englishText;
        private String vietnameseText;
    }
}
