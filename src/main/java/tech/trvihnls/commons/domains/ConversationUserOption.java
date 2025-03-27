package tech.trvihnls.commons.domains;

import jakarta.persistence.*;
import lombok.*;
import tech.trvihnls.commons.utils.enums.ConversationEntryGenderEnum;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_conversation_user_option")
public class ConversationUserOption extends BaseEntity {

    @Column(name = "english_text")
    private String englishText;

    @Column(name = "vietnamese_text")
    private String vietnameseText;

    @Column(name = "audio_url")
    private String audioUrl;

    @Enumerated(EnumType.STRING)
    private ConversationEntryGenderEnum gender;

    @ManyToOne
    @JoinColumn(name = "conversation_line_id")
    private ConversationLine conversationLine;
}
