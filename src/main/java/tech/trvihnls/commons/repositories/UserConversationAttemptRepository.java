package tech.trvihnls.commons.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tech.trvihnls.commons.domains.UserConversationAttempt;
import tech.trvihnls.commons.domains.UserConversationAttemptId;

import java.util.Optional;

@Repository
public interface UserConversationAttemptRepository extends JpaRepository<UserConversationAttempt, UserConversationAttemptId> {
    @Query("select u from UserConversationAttempt u where u.id.userId = ?1 and u.id.conversationId = ?2")
    Optional<UserConversationAttempt> findByUserIdAndConversationId(Long userId, Long conversationId);
}