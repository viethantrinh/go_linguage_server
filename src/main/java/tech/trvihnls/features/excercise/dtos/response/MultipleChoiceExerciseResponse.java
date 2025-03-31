package tech.trvihnls.features.excercise.dtos.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;
import tech.trvihnls.commons.utils.enums.LanguageEnum;
import tech.trvihnls.commons.utils.enums.MultipleChoiceExerciseEnum;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({"sourceLanguage", "targetLanguage", "questionType", "question", "options"})
public class MultipleChoiceExerciseResponse {

    private LanguageEnum sourceLanguage;
    private LanguageEnum targetLanguage;
    private MultipleChoiceExerciseEnum questionType;
    private Question question;
    @Builder.Default
    private List<Option> options = new ArrayList<>();

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonPropertyOrder({"englishText", "vietnameseText", "imageUrl", "audioUrl"})
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Question {
        private String englishText;
        private String vietnameseText;
        private String imageUrl;
        private String audioUrl;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonPropertyOrder({"optionType", "isCorrect", "englishText", "vietnameseText", "imageUrl", "audioUrl"})
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Option {
        private MultipleChoiceExerciseEnum optionType;
        @JsonProperty("isCorrect")
        private boolean isCorrect;
        private String englishText;
        private String vietnameseText;
        private String imageUrl;
        private String audioUrl;
    }
}
