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
@Table(name = "tbl_achievement")
public class Achievement extends BaseEntity {

    private String name;
    private String description;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "streak_reward")
    private int streakReward;

    @Column(name = "xp_reward")
    private int xpReward;

    @Column(name = "go_reward")
    private int goReward;
}
