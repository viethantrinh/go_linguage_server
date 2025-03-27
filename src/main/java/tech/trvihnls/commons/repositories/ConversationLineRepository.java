package tech.trvihnls.commons.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.trvihnls.commons.domains.ConversationLine;

@Repository
public interface ConversationLineRepository extends JpaRepository<ConversationLine, Long> {
}
