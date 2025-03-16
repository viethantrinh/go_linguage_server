package tech.trvihnls.commons.utils.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum XPPointsEnum {
    MAX_LEVEL_XP_POINTS(180),
    MAX_TOPIC_XP_POINTS(18),
    MAX_LESSON_XP_POINTS(3);
    private final int value;
}
