package tech.trvihnls.commons.domains;


import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class UserConversationAttemptId implements Serializable {

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "conversation_id")
    private Long conversationId;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        UserConversationAttemptId that = (UserConversationAttemptId) o;
        return Objects.equals(userId, that.userId) && Objects.equals(conversationId, that.conversationId);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(userId);
        result = 31 * result + Objects.hashCode(conversationId);
        return result;
    }
}
