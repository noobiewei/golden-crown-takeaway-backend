package com.goldencrown.takeaway_backend;

import com.stripe.Stripe;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class StripeConfig {

    @PostConstruct
    public void init() {
        String secretKey = System.getenv("STRIPE_SECRET_KEY");
        if (secretKey == null || secretKey.isBlank()) {
            System.out.println("WARNING: STRIPE_SECRET_KEY is not set — payment features will fail.");
            return;
        }
        Stripe.apiKey = secretKey;
    }
}
