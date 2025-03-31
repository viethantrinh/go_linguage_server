package tech.trvihnls.features.payment.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.trvihnls.commons.dtos.ApiResponse;
import tech.trvihnls.commons.utils.ResponseUtils;
import tech.trvihnls.commons.utils.enums.SuccessCodeEnum;
import tech.trvihnls.features.payment.dtos.request.PaymentIntentRequest;
import tech.trvihnls.features.payment.dtos.request.StripeSubscriptionRequest;
import tech.trvihnls.features.payment.dtos.response.PaymentIntentResponse;
import tech.trvihnls.features.payment.dtos.response.StripeSubscriptionResponse;
import tech.trvihnls.features.payment.services.PaymentService;

@Slf4j
@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping("/create-payment-intent")
    public ResponseEntity<ApiResponse<PaymentIntentResponse>> createPaymentIntent(@RequestBody PaymentIntentRequest request) {
        PaymentIntentResponse response = paymentService.createPaymentIntent(request);
        return ResponseUtils.success(SuccessCodeEnum.CREATE_PAYMENT_INTENT_SUCCEEDED, response);
    }

    @PostMapping("/create-stripe-subscription")
    public ResponseEntity<ApiResponse<StripeSubscriptionResponse>> createPaymentIntent(@RequestBody StripeSubscriptionRequest request) {
        StripeSubscriptionResponse response = paymentService.createStripeSubscription(request);
        return ResponseUtils.success(SuccessCodeEnum.CREATE_STRIPE_SUBSCRIPTION_SUCCEEDED, response);
    }
}
