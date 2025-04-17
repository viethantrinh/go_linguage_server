package tech.trvihnls.commons.utils.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SongCreationStatusEnum {
    lyric_created("lyric_created"),
    audio_processing("audio_processing"),
    audio_processed("audio_processed"),
    alignment_processed("alignment_processed"),
    completed("completed");
    private final String value;
}
