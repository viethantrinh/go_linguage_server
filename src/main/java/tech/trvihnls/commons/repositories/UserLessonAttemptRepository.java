package tech.trvihnls.commons.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import tech.trvihnls.commons.domains.UserLessonAttempt;
import tech.trvihnls.commons.domains.UserLessonAttemptId;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserLessonAttemptRepository extends JpaRepository<UserLessonAttempt, UserLessonAttemptId> {
  @Query("select u from UserLessonAttempt u where u.user.id = ?1")
  List<UserLessonAttempt> findByUserId(Long id);

  @Query("select u from UserLessonAttempt u where u.id.userId = ?1 and u.id.lessonId = ?2")
  Optional<UserLessonAttempt> findByUserIdAndLessonId(Long userId, Long lessonId);

  @Transactional
  @Modifying
  @Query("delete from UserLessonAttempt u where u.lesson.id = ?1")
  void deleteAllByLessonId(Long id);


}
