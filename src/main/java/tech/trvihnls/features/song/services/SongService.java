package tech.trvihnls.features.song.services;

import tech.trvihnls.features.song.dtos.response.SongDetailResponse;
import tech.trvihnls.features.song.dtos.response.SongResponse;

import java.util.List;

public interface SongService {
    SongDetailResponse getSongById(Long id);
    List<SongResponse> getAllSongs();
}
