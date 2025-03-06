package tech.trvihnls.models.dtos.payment;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StripeSubscriptionRequest {
    private Long subscriptionId;
    private String paymentMethodId;
}
