package tech.trvihnls.features.song.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.trvihnls.commons.dtos.ApiResponse;
import tech.trvihnls.commons.utils.ResponseUtils;
import tech.trvihnls.commons.utils.enums.SuccessCodeEnum;
import tech.trvihnls.features.song.dtos.response.SongDetailResponse;
import tech.trvihnls.features.song.services.SongService;

import java.util.List;

@RestController
@RequestMapping("/songs")
@RequiredArgsConstructor
public class SongController {

    private final SongService songService;

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<SongDetailResponse>> getSongById(@PathVariable String id) {
        SongDetailResponse response = songService.getSongById(Long.parseLong(id));
        return ResponseUtils.success(SuccessCodeEnum.GENERAL_SUCCESS, response);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<SongDetailResponse>>> listAllSongs() {
        var response = songService.getAllSongs();
        return ResponseUtils.success(SuccessCodeEnum.GENERAL_SUCCESS, response);
    }}
