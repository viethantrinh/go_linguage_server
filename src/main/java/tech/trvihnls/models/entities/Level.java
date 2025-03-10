package tech.trvihnls.models.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_level")
public class Level extends BaseEntity {

    @Column(name = "name", length = 64, nullable = false)
    private String name;

    @Column(name = "description", length = 256, nullable = true)
    private String description;

    @Column(name = "display_order", nullable = false)
    private int displayOrder;

    @Builder.Default
    @OneToMany(mappedBy = "level")
    private List<Topic> topics = new ArrayList<>(); // list of topics in this level
}
