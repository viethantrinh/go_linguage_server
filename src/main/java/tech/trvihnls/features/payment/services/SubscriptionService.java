package tech.trvihnls.features.payment.services;

import tech.trvihnls.commons.domains.Subscription;

public interface SubscriptionService {
    Subscription findById(Long id);
}
