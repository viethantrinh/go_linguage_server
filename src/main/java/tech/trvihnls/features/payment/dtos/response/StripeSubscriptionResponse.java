package tech.trvihnls.features.payment.dtos.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StripeSubscriptionResponse {
    private Long subscriptionId;
    private String username;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}
