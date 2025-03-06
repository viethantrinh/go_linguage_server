package tech.trvihnls.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.trvihnls.models.dtos.base.ApiResponse;
import tech.trvihnls.models.dtos.payment.PaymentIntentRequest;
import tech.trvihnls.models.dtos.payment.PaymentIntentResponse;
import tech.trvihnls.models.dtos.payment.StripeSubscriptionRequest;
import tech.trvihnls.models.dtos.payment.StripeSubscriptionResponse;
import tech.trvihnls.services.PaymentService;
import tech.trvihnls.utils.ResponseUtils;
import tech.trvihnls.utils.enums.SuccessCode;

@Slf4j
@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping("/create-payment-intent")
    public ResponseEntity<ApiResponse<PaymentIntentResponse>> createPaymentIntent(@RequestBody PaymentIntentRequest request) {
        PaymentIntentResponse response = paymentService.createPaymentIntent(request);
        return ResponseUtils.success(SuccessCode.CREATE_PAYMENT_INTENT_SUCCEEDED, response);
    }

    @PostMapping("/create-stripe-subscription")
    public ResponseEntity<ApiResponse<StripeSubscriptionResponse>> createPaymentIntent(@RequestBody StripeSubscriptionRequest request) {
        StripeSubscriptionResponse response = paymentService.createStripeSubscription(request);
        return ResponseUtils.success(SuccessCode.CREATE_STRIPE_SUBSCRIPTION_SUCCEEDED, response);
    }
}
