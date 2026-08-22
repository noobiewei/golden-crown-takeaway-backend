package com.goldencrown.takeaway_backend.order;

import com.goldencrown.takeaway_backend.menu.MenuItem;
import com.goldencrown.takeaway_backend.menu.MenuItemRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private static final BigDecimal MINIMUM_DELIVERY_ORDER = new BigDecimal("15");

    private final OrderRepository orderRepository;
    private final MenuItemRepository menuItemRepository;

    public OrderController(OrderRepository orderRepository, MenuItemRepository menuItemRepository) {
        this.orderRepository = orderRepository;
        this.menuItemRepository = menuItemRepository;
    }

    @PostMapping
    public Order createOrder(@RequestBody CreateOrderRequest request) {
        Order order = new Order(
                request.customerName(),
                request.customerPhone(),
                request.orderType(),
                request.deliveryAddress(),
                request.deliveryZone()
        );

        BigDecimal subtotal = BigDecimal.ZERO;
        for (OrderLineRequest line : request.items()) {
            MenuItem menuItem = menuItemRepository.findById(line.menuItemId())
                    .orElseThrow(() -> new IllegalArgumentException("Menu item not found: " + line.menuItemId()));

            order.addItem(new OrderItem(menuItem, line.quantity()));
            subtotal = subtotal.add(menuItem.getPrice().multiply(BigDecimal.valueOf(line.quantity())));
        }

        if (request.orderType() == OrderType.DELIVERY && subtotal.compareTo(MINIMUM_DELIVERY_ORDER) < 0) {
            throw new IllegalArgumentException(
                    "Minimum order for delivery is £" + MINIMUM_DELIVERY_ORDER + " (subtotal was £" + subtotal + ")");
        }

        BigDecimal deliveryFee = calculateDeliveryFee(request.orderType(), request.deliveryZone(), subtotal);
        order.setDeliveryFee(deliveryFee);
        order.setTotalPrice(subtotal.add(deliveryFee));

        return orderRepository.save(order);
    }

    private BigDecimal calculateDeliveryFee(OrderType orderType, DeliveryZone zone, BigDecimal subtotal) {
        if (orderType != OrderType.DELIVERY) {
            return BigDecimal.ZERO;
        }
        if (zone == null) {
            throw new IllegalArgumentException("deliveryZone is required for delivery orders");
        }
        if (zone == DeliveryZone.OVER_3_MILES) {
            return new BigDecimal("3.00");
        }
        // WITHIN_3_MILES: cheaper rate once the order is big enough to be worth the trip
        return subtotal.compareTo(MINIMUM_DELIVERY_ORDER) >= 0
                ? new BigDecimal("1.30")
                : new BigDecimal("2.00");
    }
}
