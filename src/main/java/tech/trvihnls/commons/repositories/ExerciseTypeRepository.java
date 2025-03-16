package tech.trvihnls.commons.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.trvihnls.commons.domains.Exercise;
import tech.trvihnls.commons.domains.ExerciseType;

@Repository
public interface ExerciseTypeRepository extends JpaRepository<ExerciseType, Long> {
}
