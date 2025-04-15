package tech.trvihnls.features.song.services;

import tech.trvihnls.features.song.dtos.request.SongCreateRequest;
import tech.trvihnls.features.song.dtos.response.SongCreateResponse;
import tech.trvihnls.features.song.dtos.response.SongDetailResponse;

import java.util.List;

public interface SongService {
    SongDetailResponse getSongById(Long id);
    List<SongDetailResponse> getAllSongs();
    SongCreateResponse createSongLyric(SongCreateRequest request);
}
