package tech.trvihnls.commons.domains;

import jakarta.persistence.*;
import lombok.*;
import tech.trvihnls.commons.utils.enums.SpeakerEnum;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_dialogue_exercise_line")
public class DialogueExerciseLine extends BaseEntity {

    @Enumerated(value = EnumType.STRING)
    @Column(name = "speaker", nullable = false)
    private SpeakerEnum speaker;

    @Column(name = "english_text", nullable = false)
    private String englishText;

    @Column(name = "vietnamese_text", nullable = false)
    private String vietnameseText;

    @Column(name = "audio_url", nullable = false)
    private String audioUrl;

    @Column(name = "display_order", nullable = false)
    private int displayOrder;

    @Column(name = "has_blank")
    private boolean hasBlank;

    @Column(name = "blank_word")
    private String blankWord;

    @ManyToOne
    @JoinColumn(name = "dialogue_exercise_id")
    private DialogueExercise dialogueExercise;
}
