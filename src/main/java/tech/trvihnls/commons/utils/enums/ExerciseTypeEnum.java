package tech.trvihnls.commons.utils.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ExerciseTypeEnum {
    VOCABULARY_EXERCISE(1L, "từ vựng"),
    MULTIPLE_CHOICE_EXERCISE(2L, "trắc nghiệm"),
    MATCHING_WORD_EXERCISE(3L, "nối từ"),
    WORD_ARRANGEMENT_EXERCISE(4L, "sắp xếp từ thành câu"),
    PRONOUN_ASSESSMENT_EXERCISE(5L, "phát âm"),
    DIALOGUE_EXERCISE(6L, "hội thoại");

    private final Long id;
    private final String name;
}
