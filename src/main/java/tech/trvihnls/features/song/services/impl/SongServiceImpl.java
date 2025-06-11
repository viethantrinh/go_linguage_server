package tech.trvihnls.features.song.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import tech.trvihnls.commons.domains.Song;
import tech.trvihnls.commons.exceptions.ResourceNotFoundException;
import tech.trvihnls.commons.repositories.SongRepository;
import tech.trvihnls.commons.utils.enums.ErrorCodeEnum;
import tech.trvihnls.commons.utils.enums.SongCreationStatusEnum;
import tech.trvihnls.features.ai.services.GroqService;
import tech.trvihnls.features.ai.services.SunoService;
import tech.trvihnls.features.ai.services.WhisperAlignmentService;
import tech.trvihnls.features.media.dtos.response.CloudinaryUrlResponse;
import tech.trvihnls.features.media.services.MediaUploadService;
import tech.trvihnls.features.song.dtos.request.SongCreateRequest;
import tech.trvihnls.features.song.dtos.response.*;
import tech.trvihnls.features.song.services.SongService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class SongServiceImpl implements SongService {
    private final SongRepository songRepository;
    private final GroqService groqService;
    private final SunoService sunoService;
    private final WhisperAlignmentService whisperAlignmentService;
    private final MediaUploadService mediaUploadService;


    @Override
    public SongDetailResponse getSongById(Long id) {
        Song song = songRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCodeEnum.LEARNING_MATERIAL_NOT_EXISTED));
        return SongDetailResponse.builder()
                .id(song.getId())
                .name(song.getName())
                .audioUrl(song.getAudioUrl())
                .englishLyric(song.getEnglishLyric())
                .vietnameseLyric(song.getVietnameseLyric())
                .displayOrder(song.getDisplayOrder())
                .wordTimestamp(song.getWordTimeStamp())
                .build();
    }

    @Override
    public List<SongDetailResponse> getAllSongs() {
        return songRepository.findByOrderByDisplayOrderAsc().stream()
                .map((s) -> SongDetailResponse.builder()
                        .id(s.getId())
                        .name(s.getName())
                        .audioUrl(s.getAudioUrl())
                        .englishLyric(s.getEnglishLyric())
                        .vietnameseLyric(s.getVietnameseLyric())
                        .displayOrder(s.getDisplayOrder())
                        .wordTimestamp(s.getWordTimeStamp())
                        .build()
                )
                .toList();
    }

    @Override
    public List<SongResponse> getSongs() {
        return songRepository.findByOrderByDisplayOrderAsc().stream()
                .map((s) -> SongResponse.builder()
                        .id(s.getId())
                        .name(s.getName())
                        .englishLyric(s.getEnglishLyric())
                        .vietnameseLyric(s.getVietnameseLyric())
                        .audioUrl(s.getAudioUrl())
                        .sunoTaskId(s.getSunoTaskId())
                        .creationStatus(s.getCreationStatus())
                        .createdAt(s.getCreatedAt())
                        .displayOrder(s.getDisplayOrder())
                        .build()
                )
                .toList();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public SongCreateResponse createSongLyric(SongCreateRequest request) {
        String songName = request.getName();

        Song.SongBuilder songBuilder = Song.builder();
        songBuilder.name(songName);

        if (request.getWords().isEmpty()) return SongCreateResponse.builder().build();

        List<String> words = request.getWords().stream().map(w -> w.toLowerCase().trim()).toList();

        Map<String, String> result = groqService.createEnglishAndVietnameseSong(words);

        songBuilder.englishLyric(result.get("englishLyric"));
        songBuilder.vietnameseLyric(result.get("vietnameseLyric"));
        songBuilder.sunoTaskId(null);
        songBuilder.creationStatus(SongCreationStatusEnum.lyric_created);

        Song savedSong = songRepository.save(songBuilder.build());

        return SongCreateResponse.builder()
                .id(savedSong.getId())
                .name(savedSong.getName())
                .englishLyric(savedSong.getEnglishLyric())
                .vietnameseLyric(savedSong.getVietnameseLyric())
                .sunoTaskId(savedSong.getSunoTaskId())
                .creationStatus(savedSong.getCreationStatus())
                .build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public SongCreateResponse createSongWithSuno(Long songId) {
        Song song = songRepository.findById(songId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCodeEnum.LEARNING_MATERIAL_NOT_EXISTED));
        String taskId = sunoService.generateMusic(song);
        song.setSunoTaskId(taskId);
        song.setCreationStatus(SongCreationStatusEnum.audio_processing);
        Song savedSong = songRepository.save(song);
        return SongCreateResponse.builder()
                .id(savedSong.getId())
                .name(savedSong.getName())
                .englishLyric(savedSong.getEnglishLyric())
                .vietnameseLyric(savedSong.getVietnameseLyric())
                .sunoTaskId(savedSong.getSunoTaskId())
                .creationStatus(savedSong.getCreationStatus())
                .build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public Map<String, Object> checkCreateSongWithSunoSuccess(Long songId) {
        Song song = songRepository.findById(songId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCodeEnum.LEARNING_MATERIAL_NOT_EXISTED));
        String taskId = song.getSunoTaskId();
        boolean isSuccess = sunoService.isSongCreateSuccess(taskId);
        Song savedSong = songRepository.findById(songId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCodeEnum.LEARNING_MATERIAL_NOT_EXISTED));
        Map<String, Object> result = new HashMap<>();
        if (isSuccess) {
            result.put("status", true);
            result.put("name", savedSong.getName());
            result.put("audioUrl", savedSong.getAudioUrl());
        } else {
            result.put("status", false);
            result.put("name", savedSong.getName());
            result.put("audioUrl", null);
            return result;
        }
        return result;
    }

    @Override
    public SongAfterForceAlignmentResponse forceAlignmentSongLyric(Long songId) {
        Song song = songRepository.findById(songId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCodeEnum.LEARNING_MATERIAL_NOT_EXISTED));
        Song savedAlignedSong = whisperAlignmentService.forceAlignmentSong(song);
        return SongAfterForceAlignmentResponse.builder()
                .id(savedAlignedSong.getId())
                .name(savedAlignedSong.getName())
                .audioUrl(savedAlignedSong.getAudioUrl())
                .englishLyric(savedAlignedSong.getEnglishLyric())
                .vietnameseLyric(savedAlignedSong.getVietnameseLyric())
                .timestamps(
                        savedAlignedSong.getWordTimeStamp().getWords().stream()
                                .map(w -> SongAfterForceAlignmentResponse.WordTimestamp.builder()
                                        .word(w.getWord())
                                        .startTime(w.getStart())
                                        .endTime(w.getEnd())
                                        .build()
                                )
                                .toList()
                )
                .build();
    }

    @Override
    public SongAfterUploadToCloudinaryResponse uploadSongToCloudinary(Long songId) {
        Song song = songRepository.findById(songId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCodeEnum.LEARNING_MATERIAL_NOT_EXISTED));
        // TODO: tắt để test - đã xong
        CloudinaryUrlResponse cloudinaryUrlResponse = mediaUploadService.uploadAudio(song.getAudioUrl());
        song.setAudioUrl(cloudinaryUrlResponse.getSecureUrl());
//        song.setAudioUrl("SAMPLE_URL");
        song.setCreationStatus(SongCreationStatusEnum.completed);
        Song savedSong = songRepository.save(song);
        return SongAfterUploadToCloudinaryResponse.builder()
                .id(savedSong.getId())
                .name(savedSong.getName())
                .audioUrl(savedSong.getAudioUrl())
                .englishLyric(savedSong.getEnglishLyric())
                .vietnameseLyric(savedSong.getVietnameseLyric())
                .timestamps(
                        savedSong.getWordTimeStamp().getWords().stream()
                                .map(w -> SongAfterUploadToCloudinaryResponse.WordTimestamp.builder()
                                        .word(w.getWord())
                                        .startTime(w.getStart())
                                        .endTime(w.getEnd())
                                        .build()
                                )
                                .toList()
                )
                .build();
    }
}
