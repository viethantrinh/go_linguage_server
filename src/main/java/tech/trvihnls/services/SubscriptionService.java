package tech.trvihnls.services;

import tech.trvihnls.models.entities.Subscription;

public interface SubscriptionService {
    Subscription findById(Long id);
}
