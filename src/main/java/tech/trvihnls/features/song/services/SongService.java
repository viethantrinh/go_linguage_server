package tech.trvihnls.features.song.services;

import tech.trvihnls.features.song.dtos.request.SongCreateRequest;
import tech.trvihnls.features.song.dtos.response.SongAfterForceAlignmentResponse;
import tech.trvihnls.features.song.dtos.response.SongAfterUploadToCloudinaryResponse;
import tech.trvihnls.features.song.dtos.response.SongCreateResponse;
import tech.trvihnls.features.song.dtos.response.SongDetailResponse;

import java.util.List;

public interface SongService {
    SongDetailResponse getSongById(Long id);
    List<SongDetailResponse> getAllSongs();
    SongCreateResponse createSongLyric(SongCreateRequest request);
    SongCreateResponse createSongWithSuno(Long songId);
    boolean checkCreateSongWithSunoSuccess(Long songId);
    SongAfterForceAlignmentResponse forceAlignmentSongLyric(Long songId);
    SongAfterUploadToCloudinaryResponse uploadSongToCloudinary(Long songId);
}
