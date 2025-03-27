package tech.trvihnls.commons.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.trvihnls.commons.domains.Conversation;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, Long> {
}
