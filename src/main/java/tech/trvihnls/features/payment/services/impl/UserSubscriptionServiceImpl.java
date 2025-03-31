package tech.trvihnls.features.payment.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tech.trvihnls.commons.domains.UserSubscription;
import tech.trvihnls.commons.repositories.UserSubscriptionRepository;
import tech.trvihnls.features.payment.services.UserSubscriptionService;

@Service
@RequiredArgsConstructor
public class UserSubscriptionServiceImpl implements UserSubscriptionService {
    private final UserSubscriptionRepository userSubscriptionRepository;
    @Override
    public UserSubscription createOrUpdate(UserSubscription userSubscription) {
        return userSubscriptionRepository.save(userSubscription);
    }

}
