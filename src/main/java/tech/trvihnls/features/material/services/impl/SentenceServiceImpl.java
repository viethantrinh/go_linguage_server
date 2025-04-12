package tech.trvihnls.features.material.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import tech.trvihnls.commons.domains.Exercise;
import tech.trvihnls.commons.exceptions.AppException;
import tech.trvihnls.commons.repositories.ExerciseRepository;
import tech.trvihnls.commons.repositories.MultipleChoiceExerciseRepository;
import tech.trvihnls.commons.repositories.SentenceRepository;
import tech.trvihnls.commons.utils.enums.ErrorCodeEnum;
import tech.trvihnls.commons.utils.enums.ExerciseTypeEnum;
import tech.trvihnls.features.material.dtos.query.SentenceQuery;
import tech.trvihnls.features.material.dtos.response.admin.SentenceForMultipleChoiceExerciseResponse;
import tech.trvihnls.features.material.services.SentenceService;

import java.util.*;

@Service
@RequiredArgsConstructor
public class SentenceServiceImpl implements SentenceService {

    private final ExerciseRepository exerciseRepository;
    private final SentenceRepository sentenceRepository;
    private final MultipleChoiceExerciseRepository multipleChoiceExerciseRepository;

    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public List<SentenceForMultipleChoiceExerciseResponse> getSentencesByMultipleChoiceExerciseId(Long exerciseId) {
        Optional<Exercise> exerciseOpt = exerciseRepository.findById(exerciseId);

        if (exerciseOpt.isEmpty()) {
            return Collections.emptyList();
        }

        Exercise exercise = exerciseOpt.get();

        if (!exercise.getExerciseType().getId().equals(ExerciseTypeEnum.MULTIPLE_CHOICE_EXERCISE.getId())) {
            throw new AppException(ErrorCodeEnum.RESOURCE_CONFLICT);
        }

        List<SentenceQuery> sentences = sentenceRepository.findByExerciseId(exercise.getId());

        Set<Long> sentenceIDs = multipleChoiceExerciseRepository.findAllSentencesID();

        List<SentenceForMultipleChoiceExerciseResponse> responses = new ArrayList<>();

        for (SentenceQuery sentence : sentences) {
            SentenceForMultipleChoiceExerciseResponse builtSentence = SentenceForMultipleChoiceExerciseResponse.builder()
                    .sentenceId(sentence.getId())
                    .englishText(sentence.getEnglishText())
                    .vietnameseText(sentence.getVietnameseText())
                    .audioUrl(sentence.getAudioUrl())
                    .isSelectedByAnotherExercise(false)
                    .build();

            for (Long sentenceID : sentenceIDs) {
                if (builtSentence.getSentenceId() == sentenceID) {
                    builtSentence.setSelectedByAnotherExercise(true);
                    break;
                }
            }

            responses.add(builtSentence);
        }

        return responses;
    }
}
