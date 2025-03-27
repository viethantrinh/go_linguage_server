package tech.trvihnls.mobileapis.payment.services.impl;

import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tech.trvihnls.commons.domains.Subscription;
import tech.trvihnls.commons.domains.User;
import tech.trvihnls.commons.domains.UserSubscription;
import tech.trvihnls.commons.exceptions.AppException;
import tech.trvihnls.commons.utils.SecurityUtils;
import tech.trvihnls.commons.utils.enums.ErrorCodeEnum;
import tech.trvihnls.commons.utils.enums.PaymentStatusEnum;
import tech.trvihnls.mobileapis.payment.dtos.request.PaymentIntentRequest;
import tech.trvihnls.mobileapis.payment.dtos.request.StripeSubscriptionRequest;
import tech.trvihnls.mobileapis.payment.dtos.response.PaymentIntentResponse;
import tech.trvihnls.mobileapis.payment.dtos.response.StripeSubscriptionResponse;
import tech.trvihnls.mobileapis.payment.services.PaymentService;
import tech.trvihnls.mobileapis.payment.services.SubscriptionService;
import tech.trvihnls.mobileapis.payment.services.UserSubscriptionService;
import tech.trvihnls.mobileapis.user.services.UserService;

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
            throw new AppException(ErrorCodeEnum.PAYMENT_ERROR);
        }

        // if create payment intent succeeded
        String clientSecret = paymentIntent.getClientSecret();
        return PaymentIntentResponse.builder().clientSecret(clientSecret).build();
    }

    @Override
    public StripeSubscriptionResponse createStripeSubscription(StripeSubscriptionRequest request) {
        long userId = Objects.requireNonNull(SecurityUtils.getCurrentUserId());
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

        userSubscription.setPaymentStatus(PaymentStatusEnum.SUCCEEDED.getValue());
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


