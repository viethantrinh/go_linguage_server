package tech.trvihnls.mobileapis.lesson.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tech.trvihnls.commons.domains.User;
import tech.trvihnls.commons.domains.UserLessonAttempt;
import tech.trvihnls.commons.exceptions.AppException;
import tech.trvihnls.commons.exceptions.ResourceNotFoundException;
import tech.trvihnls.commons.repositories.UserLessonAttemptRepository;
import tech.trvihnls.commons.repositories.UserRepository;
import tech.trvihnls.commons.utils.SecurityUtils;
import tech.trvihnls.commons.utils.enums.ErrorCodeEnum;
import tech.trvihnls.mobileapis.lesson.dtos.request.LessonSubmitRequest;
import tech.trvihnls.mobileapis.lesson.services.LessonService;

/**
 * Service implementation for handling lesson-related operations.
 */
@Service
@RequiredArgsConstructor
public class LessonServiceImpl implements LessonService {
    private final UserLessonAttemptRepository userLessonAttemptRepository;
    private final UserRepository userRepository;


    // TODO: draft

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
    public Object submitLesson(long lessonId, LessonSubmitRequest request) {
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
        userRepository.save(user);
        return null;
    }
}
