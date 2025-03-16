package tech.trvihnls.mobileapis.main.services;

import tech.trvihnls.commons.domains.UserSubscription;

public interface UserSubscriptionService {
    UserSubscription createOrUpdate(UserSubscription userSubscription);
}
