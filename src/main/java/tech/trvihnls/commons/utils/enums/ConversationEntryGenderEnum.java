package tech.trvihnls.commons.utils.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ConversationEntryGenderEnum {
    male("male", "nam"), female("female", "nữ");
    private final String value;
    private final String vietnameseGender;
}
