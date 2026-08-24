package com.goldencrown.takeaway_backend.order;

public record CreateOrderResponse(Order order, String checkoutUrl) {}
