package tech.trvihnls.commons.domains;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_lesson")
public class Lesson extends BaseEntity {
    @Column(name = "name", length = 64, nullable = false)
    private String name;

    @Column(name = "display_order", nullable = false)
    private int displayOrder;

    @Builder.Default
    @OneToMany(mappedBy = "lesson")
    private List<UserLessonAttempt> userLessonAttempts = new ArrayList<>(); // list of user lesson attempts for this lesson

    @ManyToOne
    @JoinColumn(name = "topic_id")
    private Topic topic;

    @ManyToOne
    @JoinColumn(name = "lesson_type_id")
    private LessonType lessonType;

    @Builder.Default
    @OneToMany(mappedBy = "lesson")
    private List<Exercise> exercises = new ArrayList<>();
}
