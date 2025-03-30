package tech.trvihnls.commons.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.trvihnls.commons.domains.Achievement;

public interface AchievementRepository extends JpaRepository<Achievement, Long> {
}