package tech.trvihnls.commons.domains;

import jakarta.persistence.*;
import lombok.*;
import tech.trvihnls.commons.utils.enums.ConversationEntryTypeEnum;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_conversation_line")
public class ConversationLine extends BaseEntity {

    @Column(name = "display_order")
    private int displayOrder;

    @Enumerated(EnumType.STRING)
    private ConversationEntryTypeEnum type;

    @Column(name = "system_english_text")
    private String systemEnglishText;

    @Column(name = "system_vietnamese_text")
    private String systemVietnameseText;

    @Column(name = "system_audio_url")
    private String systemAudioUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conversation_id")
    private Conversation conversation;

    @OneToMany(mappedBy = "conversationLine")
    private List<ConversationUserOption> conversationUserOptions = new ArrayList<>();
}
