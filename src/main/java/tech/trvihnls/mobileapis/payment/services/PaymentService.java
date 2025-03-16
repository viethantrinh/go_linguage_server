package tech.trvihnls.mobileapis.payment.services;

import tech.trvihnls.mobileapis.payment.dtos.request.PaymentIntentRequest;
import tech.trvihnls.mobileapis.payment.dtos.response.PaymentIntentResponse;
import tech.trvihnls.mobileapis.payment.dtos.request.StripeSubscriptionRequest;
import tech.trvihnls.mobileapis.payment.dtos.response.StripeSubscriptionResponse;

public interface PaymentService {
    PaymentIntentResponse createPaymentIntent(PaymentIntentRequest request);
    StripeSubscriptionResponse createStripeSubscription(StripeSubscriptionRequest request);
}
