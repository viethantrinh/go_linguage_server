package tech.trvihnls.features.ai.services;

import tech.trvihnls.commons.domains.Song;


public interface WhisperAlignmentService {
    Song forceAlignmentSong(Song song);
}
