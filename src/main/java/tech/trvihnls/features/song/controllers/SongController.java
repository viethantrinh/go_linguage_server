package tech.trvihnls.features.song.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.trvihnls.commons.dtos.ApiResponse;
import tech.trvihnls.commons.utils.ResponseUtils;
import tech.trvihnls.commons.utils.enums.SuccessCodeEnum;
import tech.trvihnls.features.song.dtos.request.SongCreateRequest;
import tech.trvihnls.features.song.dtos.response.SongAfterForceAlignmentResponse;
import tech.trvihnls.features.song.dtos.response.SongAfterUploadToCloudinaryResponse;
import tech.trvihnls.features.song.dtos.response.SongCreateResponse;
import tech.trvihnls.features.song.dtos.response.SongDetailResponse;
import tech.trvihnls.features.song.services.SongService;

import java.util.List;
import java.util.Map;

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
    }

    @PostMapping
    public ResponseEntity<ApiResponse<SongCreateResponse>> createSongWithLyric(@RequestBody SongCreateRequest request) {
        SongCreateResponse response = songService.createSongLyric(request);
        return ResponseUtils.success(SuccessCodeEnum.GENERAL_SUCCESS, response);
    }

    @PostMapping("/{id}/audio")
    public ResponseEntity<ApiResponse<SongCreateResponse>> createSongWithSuno(@PathVariable Long id) {
        var response = songService.createSongWithSuno(id);
        return ResponseUtils.success(SuccessCodeEnum.GENERAL_SUCCESS, response);
    }

    @PostMapping("/{id}/status")
    public ResponseEntity<ApiResponse<Map<String, Boolean>>> checkCreateSongWithSunoStatus(@PathVariable Long id) {
        var status = songService.checkCreateSongWithSunoSuccess(id);
        Map<String, Boolean> response = Map.of("status", status);
        return ResponseUtils.success(SuccessCodeEnum.GENERAL_SUCCESS, response);
    }

    @PostMapping("/{id}/force-alignment")
    public ResponseEntity<ApiResponse<SongAfterForceAlignmentResponse>> forceAlignmentLyric(@PathVariable Long id) {
        var response = songService.forceAlignmentSongLyric(id);
        return ResponseUtils.success(SuccessCodeEnum.GENERAL_SUCCESS, response);
    }

    @PostMapping("/{id}/upload-cloudinary")
    public ResponseEntity<ApiResponse<SongAfterUploadToCloudinaryResponse>> uploadToCloudinary(@PathVariable Long id) {
        var response = songService.uploadSongToCloudinary(id);
        return ResponseUtils.success(SuccessCodeEnum.GENERAL_SUCCESS, response);
    }
}
