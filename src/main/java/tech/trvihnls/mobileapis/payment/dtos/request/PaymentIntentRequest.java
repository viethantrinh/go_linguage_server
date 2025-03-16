package tech.trvihnls.mobileapis.payment.dtos.request;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentIntentRequest {
    private Long subscriptionId;
}
