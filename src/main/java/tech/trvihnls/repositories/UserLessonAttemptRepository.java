package tech.trvihnls.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tech.trvihnls.models.entities.UserLessonAttempt;
import tech.trvihnls.models.entities.UserLessonAttemptId;

import java.util.List;
import java.util.Set;

@Repository
public interface UserLessonAttemptRepository extends JpaRepository<UserLessonAttempt, UserLessonAttemptId> {
  @Query("select u from UserLessonAttempt u where u.user.id = ?1")
  List<UserLessonAttempt> findByUserId(Long id);

}
