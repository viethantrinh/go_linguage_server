package tech.trvihnls.features.payment.dtos.request;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentIntentRequest {
    private Long subscriptionId;
}
