package tech.trvihnls.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tech.trvihnls.exceptions.ResourceNotFoundException;
import tech.trvihnls.models.entities.Subscription;
import tech.trvihnls.repositories.SubscriptionRepository;
import tech.trvihnls.services.SubscriptionService;
import tech.trvihnls.utils.enums.ErrorCode;

@Service
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;

    @Override
    public Subscription findById(Long id) {
        return subscriptionRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException(ErrorCode.SUBSCRIPTION_NOT_EXISTED));
    }
}
