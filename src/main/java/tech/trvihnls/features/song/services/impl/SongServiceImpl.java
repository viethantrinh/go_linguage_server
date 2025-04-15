package tech.trvihnls.features.song.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import tech.trvihnls.commons.domains.Song;
import tech.trvihnls.commons.exceptions.ResourceNotFoundException;
import tech.trvihnls.commons.repositories.SongRepository;
import tech.trvihnls.commons.utils.enums.ErrorCodeEnum;
import tech.trvihnls.features.ai.services.GroqService;
import tech.trvihnls.features.song.dtos.request.SongCreateRequest;
import tech.trvihnls.features.song.dtos.response.SongCreateResponse;
import tech.trvihnls.features.song.dtos.response.SongDetailResponse;
import tech.trvihnls.features.song.services.SongService;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SongServiceImpl implements SongService {
    private final SongRepository songRepository;
    private final GroqService groqService;

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

        Song savedSong = songRepository.save(songBuilder.build());

        return SongCreateResponse.builder()
                .id(savedSong.getId())
                .name(savedSong.getName())
                .englishLyric(savedSong.getEnglishLyric())
                .vietnameseLyric(savedSong.getVietnameseLyric())
                .build();
    }
}
