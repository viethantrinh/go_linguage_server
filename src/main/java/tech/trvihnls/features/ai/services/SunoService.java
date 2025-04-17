package tech.trvihnls.features.ai.services;

import tech.trvihnls.commons.domains.Song;

public interface SunoService {
    String generateMusic(Song song);
    boolean isSongCreateSuccess(String taskId);
}
