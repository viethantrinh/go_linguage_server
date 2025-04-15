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
@Table(name = "tbl_word")
public class Word extends BaseEntity {

    @Column(name = "english_text", nullable = false)
    private String englishText;

    @Column(name = "vietnamese_text", nullable = false)
    private String vietnameseText;

    @Column(name = "audio_url")
    private String audioUrl;

    @Column(name = "image_url")
    private String imageUrl;

    @OneToOne(mappedBy = "word")
    private VocabularyExercise vocabularyExercise;

    @Builder.Default
    @ManyToMany
    @JoinTable(
            name = "tbl_word_sentence",
            joinColumns = @JoinColumn(name = "word_id"),
            inverseJoinColumns = @JoinColumn(name = "sentence_id")
    )
    private List<Sentence> sentences = new ArrayList<>();

    @ManyToMany(mappedBy = "words")
    @Builder.Default
    private List<Topic> topics = new ArrayList<>();

    public Word(Long id) {
        super(id);
    }
}
