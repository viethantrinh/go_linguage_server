package tech.trvihnls.commons.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.trvihnls.commons.domains.WordArrangementOption;

@Repository
public interface WordArrangementOptionRepository extends JpaRepository<WordArrangementOption, Long> {
}