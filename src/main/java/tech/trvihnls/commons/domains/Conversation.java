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
@Table(name = "tbl_conversation")
public class Conversation extends BaseEntity {

    private String name;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "display_order")
    private int displayOrder;

    @Builder.Default
    @OneToMany(mappedBy = "conversation", fetch = FetchType.LAZY)
    List<ConversationLine> conversationLines = new ArrayList<>();
}
