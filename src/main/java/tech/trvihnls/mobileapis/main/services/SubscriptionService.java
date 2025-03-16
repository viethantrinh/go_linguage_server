package tech.trvihnls.mobileapis.main.services;

import tech.trvihnls.commons.domains.Subscription;

public interface SubscriptionService {
    Subscription findById(Long id);
}
