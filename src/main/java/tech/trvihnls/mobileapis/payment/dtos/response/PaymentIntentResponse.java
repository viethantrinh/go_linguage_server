package tech.trvihnls.mobileapis.payment.dtos.response;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentIntentResponse {
    private String clientSecret;
}
