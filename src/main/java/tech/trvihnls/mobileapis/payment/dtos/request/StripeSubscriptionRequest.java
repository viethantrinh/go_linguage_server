package tech.trvihnls.mobileapis.payment.dtos.request;

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
