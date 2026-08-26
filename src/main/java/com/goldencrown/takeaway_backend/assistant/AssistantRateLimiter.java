package com.goldencrown.takeaway_backend.assistant;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

// Every call to the assistant costs real money (Claude API usage), and the
// endpoint is public and unauthenticated, so a simple per-IP window guards
// against a script hammering it and running up the API bill. A single
// in-memory map is fine here since this app runs as one instance — a
// distributed rate limiter (e.g. Redis-backed) would only be needed if this
// were ever horizontally scaled.
@Component
public class AssistantRateLimiter {

    private static final int MAX_REQUESTS_PER_WINDOW = 10;
    private static final Duration WINDOW = Duration.ofMinutes(10);

    private record Window(AtomicInteger count, Instant windowStart) {}

    private final ConcurrentHashMap<String, Window> windows = new ConcurrentHashMap<>();

    public boolean tryAcquire(String clientIp) {
        Instant now = Instant.now();
        Window window = windows.compute(clientIp, (ip, existing) -> {
            if (existing == null || Duration.between(existing.windowStart(), now).compareTo(WINDOW) > 0) {
                return new Window(new AtomicInteger(1), now);
            }
            existing.count().incrementAndGet();
            return existing;
        });
        return window.count().get() <= MAX_REQUESTS_PER_WINDOW;
    }

    @Scheduled(fixedRate = 30, timeUnit = java.util.concurrent.TimeUnit.MINUTES)
    void cleanup() {
        Instant cutoff = Instant.now().minus(WINDOW);
        windows.entrySet().removeIf(entry -> entry.getValue().windowStart().isBefore(cutoff));
    }
}
