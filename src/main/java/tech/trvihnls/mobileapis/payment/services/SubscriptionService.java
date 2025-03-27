package tech.trvihnls.mobileapis.payment.services;

import tech.trvihnls.commons.domains.Subscription;

public interface SubscriptionService {
    Subscription findById(Long id);
}
