package tech.trvihnls.commons.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tech.trvihnls.commons.domains.UserLessonAttempt;
import tech.trvihnls.commons.domains.UserLessonAttemptId;

import java.util.List;

@Repository
public interface UserLessonAttemptRepository extends JpaRepository<UserLessonAttempt, UserLessonAttemptId> {
  @Query("select u from UserLessonAttempt u where u.user.id = ?1")
  List<UserLessonAttempt> findByUserId(Long id);

}
