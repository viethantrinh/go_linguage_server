package tech.trvihnls.commons.utils.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum LessonTypeEnum {
    CORE_LESSON(1L, "Bài học chung"),
    SPEAKING_LESSON(2L, "Bài học nói"),
    EXAMINING_LESSON(3L, "Bài học kiểm tra");

    private final Long id;
    private final String name;
}
