package com.goldencrown.takeaway_backend.order;

import java.util.List;

public record CreateOrderRequest(
        String customerName,
        String customerPhone,
        OrderType orderType,
        String deliveryAddress,
        String deliveryPostcode,
        String specialInstructions,
        PaymentMethod paymentMethod,
        FreeDrinkChoice freeDrinkChoice,
        List<OrderLineRequest> items
) {}
