package tech.trvihnls.features.payment.services;

import tech.trvihnls.features.payment.dtos.request.PaymentIntentRequest;
import tech.trvihnls.features.payment.dtos.request.StripeSubscriptionRequest;
import tech.trvihnls.features.payment.dtos.response.PaymentIntentResponse;
import tech.trvihnls.features.payment.dtos.response.StripeSubscriptionResponse;

public interface PaymentService {
    PaymentIntentResponse createPaymentIntent(PaymentIntentRequest request);
    StripeSubscriptionResponse createStripeSubscription(StripeSubscriptionRequest request);
}
