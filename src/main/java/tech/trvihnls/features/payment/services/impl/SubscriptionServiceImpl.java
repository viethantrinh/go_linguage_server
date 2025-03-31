package tech.trvihnls.features.payment.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tech.trvihnls.commons.domains.Subscription;
import tech.trvihnls.commons.exceptions.ResourceNotFoundException;
import tech.trvihnls.commons.repositories.SubscriptionRepository;
import tech.trvihnls.commons.utils.enums.ErrorCodeEnum;
import tech.trvihnls.features.payment.services.SubscriptionService;

@Service
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;

    @Override
    public Subscription findById(Long id) {
        return subscriptionRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException(ErrorCodeEnum.SUBSCRIPTION_NOT_EXISTED));
    }
}
