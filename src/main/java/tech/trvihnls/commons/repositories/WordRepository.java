package tech.trvihnls.commons.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.trvihnls.commons.domains.Word;

@Repository
public interface WordRepository extends JpaRepository<Word, Long> {
}