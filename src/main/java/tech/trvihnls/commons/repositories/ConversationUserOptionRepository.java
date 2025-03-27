package tech.trvihnls.commons.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.trvihnls.commons.domains.ConversationUserOption;

@Repository
public interface ConversationUserOptionRepository extends JpaRepository<ConversationUserOption, Long> {
}
