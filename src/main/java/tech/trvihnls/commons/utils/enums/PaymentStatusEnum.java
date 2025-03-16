package tech.trvihnls.commons.utils.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PaymentStatusEnum {
    SUCCEEDED("succeeded"),
    PROCESS("process"),
    FAILED("failed");

    private final String value;
}
