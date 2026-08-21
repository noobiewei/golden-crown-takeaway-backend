package com.goldencrown.takeaway_backend.order;

public record OrderLineRequest(Long menuItemId, int quantity) {}
