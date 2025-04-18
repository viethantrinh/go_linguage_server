package tech.trvihnls.features.conversation.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tech.trvihnls.commons.dtos.ApiResponse;
import tech.trvihnls.commons.utils.ResponseUtils;
import tech.trvihnls.commons.utils.enums.SuccessCodeEnum;
import tech.trvihnls.features.conversation.dtos.request.ConversationCreateDto;
import tech.trvihnls.features.conversation.dtos.request.ConversationSubmitRequest;
import tech.trvihnls.features.conversation.dtos.response.ConversationDetailResponse;
import tech.trvihnls.features.conversation.dtos.response.ConversationLineResponse;
import tech.trvihnls.features.conversation.dtos.response.ConversationListResponse;
import tech.trvihnls.features.conversation.services.ConversationService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/conversations")
public class ConversationController {

    private final ConversationService conversationService;

    @GetMapping("/{id}/detail")
    public ResponseEntity<ApiResponse<List<ConversationLineResponse>>> getConversationDetail(@PathVariable String id) {
        List<ConversationLineResponse> response = conversationService.getConversationDetail(Long.parseLong(id));
        return ResponseUtils.success(SuccessCodeEnum.GENERAL_SUCCESS, response);
    }

    @PostMapping("/pronoun-assessment")
    public ResponseEntity<ApiResponse<String>> pronoun(@RequestParam("audio") MultipartFile audioFile,
                                                       @RequestParam("conversationLineId") String conversationLineId) {
        var response = conversationService.processConversationPronoun(audioFile, Long.parseLong(conversationLineId));
        return ResponseUtils.success(SuccessCodeEnum.GENERAL_SUCCESS, response);
    }

    @PostMapping("/{id}/submit")
    public ResponseEntity<ApiResponse<Object>> submitConversation(@RequestBody ConversationSubmitRequest request, @PathVariable String id) {
        var response = conversationService.submitConversation(Long.parseLong(id), request);
        return ResponseUtils.success(SuccessCodeEnum.GENERAL_SUCCESS, response);
    }

    // for admin
    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<ConversationListResponse>>> getAllConversations() {
        List<ConversationListResponse> response = conversationService.getAllConversations();
        return ResponseUtils.success(SuccessCodeEnum.GENERAL_SUCCESS, response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ConversationDetailResponse>> getConversationById(@PathVariable String id) {
        ConversationDetailResponse response = conversationService.getConversationById(Long.parseLong(id));
        return ResponseUtils.success(SuccessCodeEnum.GENERAL_SUCCESS, response);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ConversationDetailResponse>> createConversation(@RequestBody ConversationCreateDto createDto) {
        ConversationDetailResponse response = conversationService.createConversation(createDto);
        return ResponseUtils.success(SuccessCodeEnum.GENERAL_SUCCESS, response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> deleteConversation(@PathVariable String id) {
        conversationService.deleteConversation(Long.parseLong(id));
        return ResponseUtils.success(SuccessCodeEnum.GENERAL_SUCCESS, null);
    }
}
