package com.goldencrown.takeaway_backend.push;

public record PushSubscribeRequest(String endpoint, Keys keys) {
    public record Keys(String p256dh, String auth) {}
}
