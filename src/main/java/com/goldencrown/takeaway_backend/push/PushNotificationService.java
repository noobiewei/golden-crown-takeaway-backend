package com.goldencrown.takeaway_backend.push;

import nl.martijndwars.webpush.Notification;
import nl.martijndwars.webpush.PushService;
import nl.martijndwars.webpush.Subscription;
import org.apache.http.HttpResponse;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Security;

@Service
public class PushNotificationService {

    private final PushSubscriptionRepository repository;
    private final PushService pushService;

    public PushNotificationService(
            PushSubscriptionRepository repository,
            @Value("${app.vapid-public-key}") String vapidPublicKey,
            @Value("${app.vapid-private-key}") String vapidPrivateKey) {
        this.repository = repository;

        if (Security.getProvider("BC") == null) {
            Security.addProvider(new BouncyCastleProvider());
        }

        PushService service = null;
        if (!vapidPublicKey.isBlank() && !vapidPrivateKey.isBlank()) {
            try {
                service = new PushService()
                        .setSubject("mailto:limitedonlyldn@hotmail.com")
                        .setPublicKey(vapidPublicKey)
                        .setPrivateKey(vapidPrivateKey);
            } catch (Exception e) {
                System.out.println("WARNING: could not initialize push notifications: " + e.getMessage());
            }
        } else {
            System.out.println("WARNING: VAPID keys are not set — push notifications disabled.");
        }
        this.pushService = service;
    }

    public void notifyNewOrder(Long orderId, String customerName) {
        if (pushService == null) return;

        String payload = "{\"title\":\"New Order #" + orderId + "\",\"body\":\""
                + escapeJson(customerName) + "\"}";

        for (PushSubscription sub : repository.findAll()) {
            try {
                Subscription subscription = new Subscription(
                        sub.getEndpoint(), new Subscription.Keys(sub.getP256dh(), sub.getAuth()));
                Notification notification = new Notification(subscription, payload);
                HttpResponse response = pushService.send(notification);
                int status = response.getStatusLine().getStatusCode();
                if (status == 404 || status == 410) {
                    repository.delete(sub);
                }
            } catch (Exception e) {
                System.out.println("WARNING: push notification failed for subscription "
                        + sub.getId() + ": " + e.getMessage());
            }
        }
    }

    private String escapeJson(String value) {
        return value.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}
