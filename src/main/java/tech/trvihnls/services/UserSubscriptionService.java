package tech.trvihnls.services;

import tech.trvihnls.models.entities.UserSubscription;

public interface UserSubscriptionService {
    UserSubscription createOrUpdate(UserSubscription userSubscription);
}
