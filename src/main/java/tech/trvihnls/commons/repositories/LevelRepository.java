package tech.trvihnls.commons.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.trvihnls.commons.domains.Level;

@Repository
public interface LevelRepository extends JpaRepository<Level, Long> {
}
