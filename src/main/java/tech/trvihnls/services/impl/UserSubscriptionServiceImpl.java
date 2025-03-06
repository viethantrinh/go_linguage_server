package tech.trvihnls.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tech.trvihnls.models.entities.UserSubscription;
import tech.trvihnls.repositories.UserSubscriptionRepository;
import tech.trvihnls.services.UserSubscriptionService;

@Service
@RequiredArgsConstructor
public class UserSubscriptionServiceImpl implements UserSubscriptionService {
    private final UserSubscriptionRepository userSubscriptionRepository;
    @Override
    public UserSubscription createOrUpdate(UserSubscription userSubscription) {
        return userSubscriptionRepository.save(userSubscription);
    }

}
