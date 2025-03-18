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
@Table(name = "tbl_topic")
public class Topic extends BaseEntity {
    @Column(name = "name", length = 64, nullable = false)
    private String name;

    @Column(name = "description", length = 256, nullable = true)
    private String description;

    @Column(name = "image_url", length = 256, nullable = false)
    private String imageUrl;

    @Column(name = "display_order", nullable = false)
    private int displayOrder;

    @Column(name = "is_premium", nullable = false)
    private boolean isPremium; // is this topic premium or not

    @ManyToOne
    @JoinColumn(name = "level_id")
    private Level level;

    @Builder.Default
    @OneToMany(mappedBy = "topic")
    private List<Lesson> lessons = new ArrayList<>(); // list of lessons in this topic

    @Builder.Default
    @ManyToMany
    @JoinTable(
            name = "tbl_topic_word",
            joinColumns = @JoinColumn(name = "topic_id"),
            inverseJoinColumns = @JoinColumn(name = "word_id")
    )
    private List<Word> words = new ArrayList<>();

    @Builder.Default
    @ManyToMany
    @JoinTable(
            name = "tbl_topic_sentence",
            joinColumns = @JoinColumn(name = "topic_id"),
            inverseJoinColumns = @JoinColumn(name = "sentence_id")
    )
    private List<Sentence> sentences = new ArrayList<>();
}
