package com.goldencrown.takeaway_backend.order;

import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.net.Webhook;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/webhooks")
public class StripeWebhookController {

    private final OrderRepository orderRepository;
    private final ObjectMapper objectMapper;

    public StripeWebhookController(OrderRepository orderRepository, ObjectMapper objectMapper) {
        this.orderRepository = orderRepository;
        this.objectMapper = objectMapper;
    }

    @PostMapping("/stripe")
    public void handleStripeWebhook(
            @RequestBody String payload,
            @RequestHeader("Stripe-Signature") String sigHeader) {

        String webhookSecret = System.getenv("STRIPE_WEBHOOK_SECRET");
        if (webhookSecret == null || webhookSecret.isBlank()) {
            throw new IllegalStateException("STRIPE_WEBHOOK_SECRET is not configured");
        }

        Event event;
        try {
            event = Webhook.constructEvent(payload, sigHeader, webhookSecret);
        } catch (SignatureVerificationException e) {
            throw new IllegalArgumentException("Invalid Stripe webhook signature");
        }

        if ("checkout.session.completed".equals(event.getType())) {
            // Using the typed getObject() here is unreliable: it silently returns
            // empty (no exception) when the event's API version doesn't match what
            // this SDK version expects. Reading the raw JSON avoids that entirely.
            JsonNode dataObject = objectMapper.readTree(event.getDataObjectDeserializer().getRawJson());
            String sessionId = dataObject.get("id").asString();
            orderRepository.findByStripeSessionId(sessionId).ifPresent(order -> {
                order.setPaymentStatus(PaymentStatus.PAID);
                orderRepository.save(order);
            });
        }
    }
}
