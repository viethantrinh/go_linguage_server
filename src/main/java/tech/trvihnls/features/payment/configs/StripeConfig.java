package tech.trvihnls.features.payment.configs;

import com.stripe.Stripe;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StripeConfig {
    @Value("${stripe.secret-key}")
    private String stripeSecretKey;

    @PostConstruct
    public void setUpStripeKey() {
        Stripe.apiKey = stripeSecretKey;
    }
}
