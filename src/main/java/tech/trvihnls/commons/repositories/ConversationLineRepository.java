package tech.trvihnls.commons.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tech.trvihnls.commons.domains.ConversationLine;

import java.util.List;

@Repository
public interface ConversationLineRepository extends JpaRepository<ConversationLine, Long> {
    @Query("select c from ConversationLine c where c.conversation.id = :id order by c.displayOrder")
    List<ConversationLine> findByConversationIdOrderByDisplayOrderAsc(@Param("id") Long id);
}
