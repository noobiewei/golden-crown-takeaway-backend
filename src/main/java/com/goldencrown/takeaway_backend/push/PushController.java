package com.goldencrown.takeaway_backend.push;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/admin/push")
public class PushController {

    private final PushSubscriptionRepository repository;

    @Value("${app.vapid-public-key}")
    private String vapidPublicKey;

    public PushController(PushSubscriptionRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/vapid-public-key")
    public Map<String, String> getPublicKey() {
        return Map.of("publicKey", vapidPublicKey);
    }

    @PostMapping("/subscribe")
    public void subscribe(@RequestBody PushSubscribeRequest request) {
        if (repository.findByEndpoint(request.endpoint()).isEmpty()) {
            repository.save(new PushSubscription(
                    request.endpoint(), request.keys().p256dh(), request.keys().auth()));
        }
    }

    @DeleteMapping("/subscribe")
    public void unsubscribe(@RequestBody PushSubscribeRequest request) {
        repository.findByEndpoint(request.endpoint()).ifPresent(repository::delete);
    }
}
