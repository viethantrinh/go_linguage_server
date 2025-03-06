package tech.trvihnls.utils.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PaymentStatus {
    SUCCEEDED("succeeded"),
    PROCESS("process"),
    FAILED("failed");

    private final String value;
}
