package tech.trvihnls.mobileapis.payment.services;

import tech.trvihnls.commons.domains.UserSubscription;

public interface UserSubscriptionService {
    UserSubscription createOrUpdate(UserSubscription userSubscription);
}
