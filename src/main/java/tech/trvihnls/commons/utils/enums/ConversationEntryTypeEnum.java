package tech.trvihnls.commons.utils.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ConversationEntryTypeEnum {
    system("system"), user("user");

    private final String value;
}
