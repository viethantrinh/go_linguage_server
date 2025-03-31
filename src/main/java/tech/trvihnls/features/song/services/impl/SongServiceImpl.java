package tech.trvihnls.features.song.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tech.trvihnls.commons.domains.Song;
import tech.trvihnls.commons.exceptions.ResourceNotFoundException;
import tech.trvihnls.commons.repositories.SongRepository;
import tech.trvihnls.commons.utils.enums.ErrorCodeEnum;
import tech.trvihnls.features.song.dtos.response.SongDetailResponse;
import tech.trvihnls.features.song.dtos.response.SongResponse;
import tech.trvihnls.features.song.services.SongService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SongServiceImpl implements SongService {


    private final SongRepository songRepository;

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
    public List<SongResponse> getAllSongs() {
        return songRepository.findByOrderByDisplayOrderAsc().stream()
                .map((s) -> SongResponse.builder()
                        .id(s.getId())
                        .name(s.getName())
                        .displayOrder(s.getDisplayOrder())
                        .build()
                )
                .toList();
    }
}
