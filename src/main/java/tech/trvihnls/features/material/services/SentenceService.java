package tech.trvihnls.features.material.services;

import tech.trvihnls.features.material.dtos.response.admin.SentenceForMultipleChoiceExerciseResponse;
import tech.trvihnls.features.material.dtos.response.admin.SentenceForWordArrangementExerciseResponse;

import java.util.List;

public interface SentenceService {
    List<SentenceForMultipleChoiceExerciseResponse> getSentencesByMultipleChoiceExerciseId(Long exerciseId);
    List<SentenceForWordArrangementExerciseResponse> getSentencesByWordArrangementExerciseId(Long exerciseId);
}
