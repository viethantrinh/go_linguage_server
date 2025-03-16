package tech.trvihnls.commons.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tech.trvihnls.commons.domains.Sentence;

import java.util.List;

@Repository
public interface SentenceRepository extends JpaRepository<Sentence, Long> {

}