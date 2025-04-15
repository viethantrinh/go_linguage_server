package tech.trvihnls.commons.domains;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
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
@Table(name = "tbl_sentence")
public class Sentence extends BaseEntity {
    @Column(name = "english_text", nullable = false)
    private String englishText;

    @Column(name = "vietnamese_text", nullable = false)
    private String vietnameseText;

    @Column(name = "audio_url")
    private String audioUrl;

    @ManyToMany(mappedBy = "sentences")
    @Builder.Default
    private List<Topic> topics = new ArrayList<>();

    @ManyToMany(mappedBy = "sentences")
    @Builder.Default
    private List<Word> words = new ArrayList<>();
}
