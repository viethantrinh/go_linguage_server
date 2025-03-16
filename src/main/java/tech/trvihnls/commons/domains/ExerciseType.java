package tech.trvihnls.commons.domains;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_exercise_type")
public class ExerciseType extends BaseEntity {
    @Column(name = "name", length = 50, nullable = false, unique = true)
    private String name;

    @Column(name = "description", length = 256)
    private String description;
}
