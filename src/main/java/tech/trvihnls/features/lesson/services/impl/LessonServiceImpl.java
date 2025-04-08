package tech.trvihnls.features.lesson.services.impl;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.trvihnls.commons.domains.*;
import tech.trvihnls.commons.exceptions.AppException;
import tech.trvihnls.commons.exceptions.ResourceNotFoundException;
import tech.trvihnls.commons.repositories.*;
import tech.trvihnls.commons.utils.SecurityUtils;
import tech.trvihnls.commons.utils.enums.ErrorCodeEnum;
import tech.trvihnls.features.achievement.dtos.response.AchievementResponse;
import tech.trvihnls.features.lesson.dtos.request.LessonCreateAdminRequest;
import tech.trvihnls.features.lesson.dtos.request.LessonSubmitRequest;
import tech.trvihnls.features.lesson.dtos.request.LessonUpdateAdminRequest;
import tech.trvihnls.features.lesson.dtos.request.LessonUpdateExerciseAdminRequest;
import tech.trvihnls.features.lesson.dtos.response.*;
import tech.trvihnls.features.lesson.services.LessonService;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Service implementation for handling lesson-related operations.
 */
@Service
@RequiredArgsConstructor
public class LessonServiceImpl implements LessonService {
    private final UserLessonAttemptRepository userLessonAttemptRepository;
    private final UserRepository userRepository;
    private final AchievementRepository achievementRepository;
    private final LessonRepository lessonRepository;
    private final ExerciseRepository exerciseRepository;
    private final EntityManager entityManager;
    private final TopicRepository topicRepository;


    /**
     * Submits a lesson attempt for a user.
     *
     * @param lessonId the ID of the lesson being submitted
     * @param request  the request containing submission details
     * @return an object representing the result of the submission
     * @throws ResourceNotFoundException if the user or lesson attempt is not found
     * @throws AppException              if the XP points are invalid
     */
    @Override
    public LessonSubmitResponse submitLesson(long lessonId, LessonSubmitRequest request) {
        Long uid = SecurityUtils.getCurrentUserId();

        assert uid != null;
        User user = userRepository.findById(uid)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCodeEnum.USER_NOT_EXISTED));

        UserLessonAttempt userLessonAttempt = userLessonAttemptRepository.findByUserIdAndLessonId(uid, lessonId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCodeEnum.LEARNING_MATERIAL_NOT_EXISTED));

        // handle xp points earned (update)
        int newXpPoints = request.getXpPoints();
        int currentXpPointsEarned = userLessonAttempt.getXpPointsEarned();

        if (newXpPoints < 0 || newXpPoints > 3) { // maximum xp points is between 0 and 3 (0, 1, 2, 3)
            throw new AppException(ErrorCodeEnum.RESOURCE_CONFLICT);
        }

        if (newXpPoints == currentXpPointsEarned || newXpPoints < currentXpPointsEarned) { // stay the same points as the old one
            newXpPoints = currentXpPointsEarned;
        }

        userLessonAttempt.setXpPointsEarned(newXpPoints); // if newXpPoint > currentXpPoints => update newXpPoint to the attempt record

        user.setTotalXPPoints(user.getTotalXPPoints() + (newXpPoints - currentXpPointsEarned)); // after that, update the totalPoint of user

        // handle go points earned (update)
        int newGoPointsEarned = request.getGoPoints();
        userLessonAttempt.setGoPointsEarned(newGoPointsEarned);
        user.setTotalGoPoints(user.getTotalGoPoints() + newGoPointsEarned);

        // handle streak points (+1)
        user.setTotalStreakPoints(user.getTotalStreakPoints() + 1);


        userLessonAttemptRepository.save(userLessonAttempt);
        User savedUser = userRepository.save(user);

        // if achieve any achievement, handle here
        List<Achievement> achievements = achievementRepository.findAll();
        List<AchievementResponse> achievementResponses = new ArrayList<>();
        for (Achievement a : achievements) {
            if (savedUser.getTotalGoPoints() >= a.getGoRewardCondition() && !savedUser.getAchievements().contains(a)) {
                AchievementResponse achievementResponse = AchievementResponse.builder()
                        .name(a.getName())
                        .description(a.getDescription())
                        .imageUrl(a.getImageUrl())
                        .build();
                achievementResponses.add(achievementResponse);
                savedUser.getAchievements().add(a);
                userRepository.save(user);
            }
        }

        return LessonSubmitResponse.builder()
                .achievements(achievementResponses)
                .build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public List<LessonAdminResponse> getAllLessons() {
        List<Lesson> lessons = lessonRepository.findAll();

        return lessons.stream()
                .map(lesson -> LessonAdminResponse.builder()
                        .id(lesson.getId())
                        .lessonTypeId(lesson.getLessonType() != null ? lesson.getLessonType().getId() : 0)
                        .name(lesson.getName())
                        .topicName(lesson.getTopic() != null ? lesson.getTopic().getName() : "")
                        .displayOrder(lesson.getDisplayOrder())
                        .createdAt(lesson.getCreatedAt())
                        .build())
                .collect(Collectors.toList());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    @Override
    public void deleteLessonById(Long id) {
        Lesson lesson = lessonRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCodeEnum.LEARNING_MATERIAL_NOT_EXISTED));

        // delete all references to this lesson
        userLessonAttemptRepository.deleteAllByLessonId(lesson.getId());

        // delete all exercises of this lesson
        List<Exercise> exercises = exerciseRepository.findByLessonId(lesson.getId());
        for (Exercise exercise : exercises) {
            deleteExerciseRelationships(exercise);
        }

        // delete the lesson its self
        entityManager.createNativeQuery(
                        "DELETE FROM tbl_lesson WHERE tbl_lesson.id = :lessonId")
                .setParameter("lessonId", lesson.getId())
                .executeUpdate();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public LessonCreateAdminResponse createLesson(LessonCreateAdminRequest request) {
        // Create and save the main lesson
        Lesson lesson = Lesson.builder()
                .name(request.getName())
                .lessonType(new LessonType(request.getLessonTypeId()))
                .topic(new Topic(request.getTopicId()))
                .displayOrder(calculateLessonDisplayOrderByTopicId(request.getTopicId()))
                .build();

        Lesson savedLesson = lessonRepository.save(lesson);

        // Process exercises if they exist in the request
        if (request.getExercises() != null && !request.getExercises().isEmpty()) {
            for (var exerciseRequest : request.getExercises()) {
                Exercise exercise = Exercise.builder()
                        .instruction(exerciseRequest.getName())
                        .exerciseType(new ExerciseType(exerciseRequest.getExerciseTypeId()))
                        .displayOrder(exerciseRequest.getDisplayOrder())
                        .lesson(savedLesson)
                        .build();
                exerciseRepository.save(exercise);
            }
        }

        return LessonCreateAdminResponse.builder()
                .id(savedLesson.getId())
                .lessonTypeId(savedLesson.getLessonType().getId())
                .name(savedLesson.getName())
                .topicName(topicRepository.findById(request.getTopicId()).orElse(null).getName())
                .displayOrder(savedLesson.getDisplayOrder())
                .createdAt(savedLesson.getCreatedAt())
                .build();
    }

    private int calculateLessonDisplayOrderByTopicId(long topicId) {
        List<Lesson> lessons = lessonRepository.findByTopicIdOrderByDisplayOrderAsc(topicId);

        int maximumOrder = lessons.stream()
                .mapToInt(Lesson::getDisplayOrder)
                .max()
                .orElse(0);

        return maximumOrder + 1;
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public LessonDetailAdminResponse getLessonDetail(Long id) {
        Lesson lesson = lessonRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCodeEnum.LEARNING_MATERIAL_NOT_EXISTED));

        List<Exercise> exercises = exerciseRepository.findByLessonId(lesson.getId());
        List<LessonDetailExerciseAdminResponse> exerciseResponses = exercises.stream()
                .map(exercise -> LessonDetailExerciseAdminResponse.builder()
                        .id(exercise.getId())
                        .name(exercise.getInstruction())
                        .exerciseTypeId(exercise.getExerciseType().getId())
                        .displayOrder(exercise.getDisplayOrder())
                        .build())
                .sorted(Comparator.comparingInt(LessonDetailExerciseAdminResponse::getDisplayOrder))
                .collect(Collectors.toList());

        return LessonDetailAdminResponse.builder()
                .id(lesson.getId())
                .name(lesson.getName())
                .lessonTypeId(lesson.getLessonType().getId())
                .topicId(lesson.getTopic().getId())
                .exercises(exerciseResponses)
                .build();
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public LessonUpdateAdminResponse updateLesson(LessonUpdateAdminRequest request) {
        Long lessonId = request.getId();
        Lesson lessonToUpdate = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCodeEnum.LEARNING_MATERIAL_NOT_EXISTED));

        lessonToUpdate.setName(request.getName());
        lessonToUpdate.setLessonType(new LessonType(request.getLessonTypeId()));
        lessonToUpdate.setTopic(new Topic(request.getTopicId()));

        List<Exercise> existingExercises = lessonToUpdate.getExercises();
        Map<Long, Exercise> existingExercisesMap = existingExercises.stream()
                .collect(Collectors.toMap(Exercise::getId, exercise -> exercise));


        // Handle existing exercises - update them directly
        for (LessonUpdateExerciseAdminRequest exerciseRequest : request.getExercises()) {
            if (exerciseRequest.getId() != null && exerciseRequest.getId() > 0) {
                Exercise exercise = existingExercisesMap.get(exerciseRequest.getId());
                if (exercise != null) {
                    // Update existing exercise
                    exercise.setInstruction(exerciseRequest.getName());
                    exercise.setExerciseType(new ExerciseType(exerciseRequest.getExerciseTypeId()));
                    exercise.setDisplayOrder(exerciseRequest.getDisplayOrder());
                    exerciseRepository.save(exercise); // Save each update
                }
            } else {
                // Add new exercise with null or 0 id
                Exercise newExercise = Exercise.builder()
                        .instruction(exerciseRequest.getName())
                        .exerciseType(new ExerciseType(exerciseRequest.getExerciseTypeId()))
                        .displayOrder(exerciseRequest.getDisplayOrder())
                        .lesson(lessonToUpdate)
                        .build();
                exerciseRepository.save(newExercise);
            }
        }

        // Update exercises from the request
        Set<Long> exerciseIdsToUpdate = request.getExercises().stream()
                .map(LessonUpdateExerciseAdminRequest::getId)
                .filter(id -> id != null && id > 0) // Only consider valid IDs
                .collect(Collectors.toSet());

        // Find and delete exercises that aren't in the update request
        List<Exercise> orphanExercises = existingExercises.stream()
                .filter(exercise -> !exerciseIdsToUpdate.contains(exercise.getId()))
                .toList();

        for (Exercise exercise : orphanExercises) {
            deleteExerciseRelationships(exercise);
        }

        // We don't need to reset lessonToUpdate.setExercises() since the relationships
        // are maintained through the foreign key in exercises
        Lesson savedLesson = lessonRepository.save(lessonToUpdate);

        return LessonUpdateAdminResponse.builder()
                .id(savedLesson.getId())
                .lessonTypeId(savedLesson.getLessonType().getId())
                .name(savedLesson.getName())
                .topicName(savedLesson.getTopic().getName())
                .displayOrder(savedLesson.getDisplayOrder())
                .createdAt(savedLesson.getCreatedAt())
                .build();
    }

    private void deleteExerciseRelationships(Exercise exercise) {
        // For vocabulary exercises: need to delete (not nullify) due to composite key
        if (exercise.getVocabularyExercise() != null) {
            entityManager.createNativeQuery(
                            "DELETE FROM tbl_vocabulary_exercise WHERE exercise_id = :exerciseId")
                    .setParameter("exerciseId", exercise.getId())
                    .executeUpdate();
        }

        // For dialogue exercises
        if (exercise.getDialogueExercise() != null) {
            Long deId = exercise.getDialogueExercise().getId();

            // Delete dialogue exercise lines first
            entityManager.createNativeQuery(
                            "DELETE FROM tbl_dialogue_exercise_line WHERE dialogue_exercise_id = :deId")
                    .setParameter("deId", deId)
                    .executeUpdate();

            // Then delete the dialogue exercise itself
            entityManager.createNativeQuery(
                            "DELETE FROM tbl_dialogue_exercise WHERE id = :deId")
                    .setParameter("deId", deId)
                    .executeUpdate();
        }

        // For multiple choice exercises
        if (exercise.getMultipleChoiceExercise() != null) {
            Long mceId = exercise.getMultipleChoiceExercise().getId();

            // Delete options first
            entityManager.createNativeQuery(
                            "DELETE FROM tbl_multiple_choice_option WHERE multiple_choice_exercise_id = :mceId")
                    .setParameter("mceId", mceId)
                    .executeUpdate();

            // Then delete the exercise itself
            entityManager.createNativeQuery(
                            "DELETE FROM tbl_multiple_choice_exercise WHERE id = :mceId")
                    .setParameter("mceId", mceId)
                    .executeUpdate();
        }

        // For word arrangement exercises
        if (exercise.getWordArrangementExercise() != null) {
            Long waeId = exercise.getWordArrangementExercise().getId();

            // Delete options first
            entityManager.createNativeQuery(
                            "DELETE FROM tbl_word_arrangement_option WHERE word_arrangement_exercise_id = :waeId")
                    .setParameter("waeId", waeId)
                    .executeUpdate();

            // Then delete the exercise
            entityManager.createNativeQuery(
                            "DELETE FROM tbl_word_arrangement_exercise WHERE id = :waeId")
                    .setParameter("waeId", waeId)
                    .executeUpdate();
        }

        // For matching exercises
        if (exercise.getMatchingExercise() != null) {
            Long meId = exercise.getMatchingExercise().getId();

            // Delete matching pairs first
            entityManager.createNativeQuery(
                            "DELETE FROM tbl_matching_pair WHERE matching_exercise_id = :meId")
                    .setParameter("meId", meId)
                    .executeUpdate();

            // Then delete the exercise
            entityManager.createNativeQuery(
                            "DELETE FROM tbl_matching_exercise WHERE id = :meId")
                    .setParameter("meId", meId)
                    .executeUpdate();
        }

        // Delete the base exercise
        entityManager.createNativeQuery(
                        "DELETE FROM tbl_exercise WHERE id = :exerciseId")
                .setParameter("exerciseId", exercise.getId())
                .executeUpdate();
    }
}
