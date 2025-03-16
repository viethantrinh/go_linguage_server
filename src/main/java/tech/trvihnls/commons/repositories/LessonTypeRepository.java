package tech.trvihnls.commons.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.trvihnls.commons.domains.LessonType;

@Repository
public interface LessonTypeRepository extends JpaRepository<LessonType, Long> {
}
