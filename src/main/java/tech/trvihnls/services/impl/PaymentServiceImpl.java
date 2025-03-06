package tech.trvihnls.services.impl;

import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tech.trvihnls.exceptions.AppException;
import tech.trvihnls.models.dtos.payment.PaymentIntentRequest;
import tech.trvihnls.models.dtos.payment.PaymentIntentResponse;
import tech.trvihnls.models.dtos.payment.StripeSubscriptionRequest;
import tech.trvihnls.models.dtos.payment.StripeSubscriptionResponse;
import tech.trvihnls.models.entities.Subscription;
import tech.trvihnls.models.entities.User;
import tech.trvihnls.models.entities.UserSubscription;
import tech.trvihnls.services.PaymentService;
import tech.trvihnls.services.SubscriptionService;
import tech.trvihnls.services.UserService;
import tech.trvihnls.services.UserSubscriptionService;
import tech.trvihnls.utils.SecurityUtils;
import tech.trvihnls.utils.enums.ErrorCode;
import tech.trvihnls.utils.enums.PaymentStatus;

import java.time.LocalDateTime;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final SubscriptionService subscriptionService;
    private final UserService userService;
    private final UserSubscriptionService userSubscriptionService;

    @Value("${stripe.web-hook-key}")
    private String webhookSecret;


    @Override
    public PaymentIntentResponse createPaymentIntent(PaymentIntentRequest request) {
        Subscription subscription = subscriptionService.findById(request.getSubscriptionId());

        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount(Integer.toUnsignedLong(subscription.getPrice()))
                .setCurrency("vnd")
                .setAutomaticPaymentMethods(PaymentIntentCreateParams.AutomaticPaymentMethods.builder().setEnabled(true).build())
                .build();

        PaymentIntent paymentIntent;
        try {
            paymentIntent = PaymentIntent.create(params);
        } catch (StripeException e) {
            throw new AppException(ErrorCode.PAYMENT_ERROR);
        }

        // if create payment intent succeeded
        String clientSecret = paymentIntent.getClientSecret();
        return PaymentIntentResponse.builder().clientSecret(clientSecret).build();
    }

    @Override
    public StripeSubscriptionResponse createStripeSubscription(StripeSubscriptionRequest request) {
        long userId = Long.parseLong(Objects.requireNonNull(SecurityUtils.getCurrentUserId()));
        User user = userService.findById(userId);
        Subscription subscription = subscriptionService.findById(request.getSubscriptionId());

        // Save subscription details in our database
        UserSubscription userSubscription = new UserSubscription();
        userSubscription.setUser(user);
        userSubscription.setSubscription(subscription);
        userSubscription.setStartDate(LocalDateTime.now());

        if ("Trọn đời".equals(subscription.getName())) {
            userSubscription.setEndDate(LocalDateTime.now().plusYears(99));
        } else {
            userSubscription.setEndDate(LocalDateTime.now().plusMonths(subscription.getDurationInMonth()));
        }

        userSubscription.setPaymentStatus(PaymentStatus.SUCCEEDED.getValue());
        userSubscription.setActive(true);
        userSubscription.setStripePaymentIntentId(request.getPaymentMethodId());

        UserSubscription savedUS = userSubscriptionService.createOrUpdate(userSubscription);

        return StripeSubscriptionResponse.builder()
                .subscriptionId(savedUS.getId())
                .username(user.getName())
                .startDate(savedUS.getStartDate())
                .endDate(savedUS.getEndDate())
                .build();
    }
}


