package tech.trvihnls.commons.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tech.trvihnls.commons.domains.ConversationUserOption;

import java.util.List;

@Repository
public interface ConversationUserOptionRepository extends JpaRepository<ConversationUserOption, Long> {
    @Query("select c from ConversationUserOption c where c.conversationLine.id = :id")
    List<ConversationUserOption> findByConversationLineId(@Param("id") Long id);
}
