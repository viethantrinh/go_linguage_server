package tech.trvihnls.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import tech.trvihnls.models.entities.Level;

@Repository
public interface LevelRepository extends JpaRepository<Level, Long> {
}
