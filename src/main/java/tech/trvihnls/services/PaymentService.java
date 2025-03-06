package tech.trvihnls.services;

import tech.trvihnls.models.dtos.payment.PaymentIntentRequest;
import tech.trvihnls.models.dtos.payment.PaymentIntentResponse;
import tech.trvihnls.models.dtos.payment.StripeSubscriptionRequest;
import tech.trvihnls.models.dtos.payment.StripeSubscriptionResponse;

public interface PaymentService {
    PaymentIntentResponse createPaymentIntent(PaymentIntentRequest request);
    StripeSubscriptionResponse createStripeSubscription(StripeSubscriptionRequest request);
}
