package tech.trvihnls.models.dtos.user;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoResponse {
    private Long id;
    private String name;
    private String email;
    private String imageUrl;
    private int totalStreakPoints;
    private int totalXPPoints;
    private int totalGoPoints;
}
