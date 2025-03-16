package tech.trvihnls.commons.domains;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_user_lesson_attempt")
public class UserLessonAttempt {

    @EmbeddedId
    private UserLessonAttemptId id;

    @Builder.Default
    @Column(name = "xp_points_earned", nullable = false)
    private int xpPointsEarned = 0;

    @Builder.Default
    @Column(name = "go_points_earned", nullable = false)
    private int goPointsEarned = 0;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", insertable = false)
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "lesson_id", insertable = false, updatable = false)
    private Lesson lesson;

}
