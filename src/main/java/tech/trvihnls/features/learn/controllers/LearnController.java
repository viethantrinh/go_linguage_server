package tech.trvihnls.features.learn.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import tech.trvihnls.commons.dtos.ApiResponse;
import tech.trvihnls.commons.utils.ResponseUtils;
import tech.trvihnls.commons.utils.enums.SuccessCodeEnum;
import tech.trvihnls.features.learn.dtos.response.PronounAssessmentResponse;
import tech.trvihnls.features.learn.services.PronounAssessmentService;

@RestController
@RequestMapping("/learns")
@RequiredArgsConstructor
@Validated
public class LearnController {

    private final PronounAssessmentService assessmentService;

    @PostMapping("/pronoun-assessment")
    public ResponseEntity<ApiResponse<Object>> assessPronoun(@RequestParam("audio") MultipartFile audioFile,
                                                             @RequestParam("text") String referenceText) {
        PronounAssessmentResponse pronounAssessmentResponse = assessmentService.assessPronunciation(audioFile, referenceText);
        return ResponseUtils.success(SuccessCodeEnum.GENERAL_SUCCESS, pronounAssessmentResponse);
    }
}
