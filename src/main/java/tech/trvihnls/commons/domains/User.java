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
@Table(name = "tbl_user")
public class User extends BaseEntity {

    @Column(name = "name", length = 64)
    private String name;

    @Column(name = "email", length = 128, nullable = false, unique = true)
    private String email;

    @Column(name = "password", length = 256, unique = true)
    private String password;

    @Builder.Default
    @Column(name = "enabled")
    private boolean enabled = true;

    @Builder.Default
    @Column(name = "total_streak_points")
    private int totalStreakPoints = 0;

    @Builder.Default
    @Column(name = "total_xp_points")
    private int totalXPPoints = 0;

    @Builder.Default
    @Column(name = "total_go_points")
    private int totalGoPoints = 0;

    @Builder.Default
    @ManyToMany
    @JoinTable(
            name = "tbl_user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private List<Role> roles = new ArrayList<>(); // what are roles this user have

    @Builder.Default
    @OneToMany(mappedBy = "user")
    private List<UserSubscription> userSubscriptions = new ArrayList<>(); // subscriptions list of this user have

    @Builder.Default
    @OneToMany(mappedBy = "user")
    private List<UserLessonAttempt> userLessonAttempts = new ArrayList<>(); // lessons attempts of this user

    @Builder.Default
    @ManyToMany
    @JoinTable(
            name = "tbl_user_achievement",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "achievement_id")
    )
    private List<Achievement> achievements = new ArrayList<>();
}
