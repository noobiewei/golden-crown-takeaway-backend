package com.goldencrown.takeaway_backend.order;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByOrderByCreatedAtDesc();
    Optional<Order> findByStripeSessionId(String stripeSessionId);
}
