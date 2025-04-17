package tech.trvihnls.features.song.services;

import tech.trvihnls.features.song.dtos.request.SongCreateRequest;
import tech.trvihnls.features.song.dtos.response.*;

import java.util.List;
import java.util.Map;

public interface SongService {
    SongDetailResponse getSongById(Long id);
    List<SongDetailResponse> getAllSongs();
    List<SongResponse> getSongs();
    SongCreateResponse createSongLyric(SongCreateRequest request);
    SongCreateResponse createSongWithSuno(Long songId);
    Map<String, Object> checkCreateSongWithSunoSuccess(Long songId);
    SongAfterForceAlignmentResponse forceAlignmentSongLyric(Long songId);
    SongAfterUploadToCloudinaryResponse uploadSongToCloudinary(Long songId);
}
