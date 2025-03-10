package tech.trvihnls.models.entities;


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
public class UserLessonAttemptId implements Serializable {

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "lesson_id")
    private Long lessonId;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        UserLessonAttemptId that = (UserLessonAttemptId) o;
        return Objects.equals(userId, that.userId) && Objects.equals(lessonId, that.lessonId);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(userId);
        result = 31 * result + Objects.hashCode(lessonId);
        return result;
    }
}
