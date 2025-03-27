package tech.trvihnls.mobileapis.conversation.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tech.trvihnls.commons.dtos.ApiResponse;
import tech.trvihnls.commons.utils.ResponseUtils;
import tech.trvihnls.commons.utils.enums.SuccessCodeEnum;
import tech.trvihnls.mobileapis.conversation.dtos.response.ConversationLineResponse;
import tech.trvihnls.mobileapis.conversation.services.ConversationService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/conversation")
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
}
