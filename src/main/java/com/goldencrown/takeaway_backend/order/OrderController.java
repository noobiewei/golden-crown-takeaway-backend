package com.goldencrown.takeaway_backend.order;

import com.goldencrown.takeaway_backend.menu.MenuItem;
import com.goldencrown.takeaway_backend.menu.MenuItemRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private static final BigDecimal MINIMUM_DELIVERY_ORDER = new BigDecimal("15");
    private static final BigDecimal STANDARD_DELIVERY_FEE = new BigDecimal("1.30");
    private static final BigDecimal HIGHER_DELIVERY_FEE = new BigDecimal("3.00");

    // Postcode prefixes (outward code, optionally + sector digit, spaces removed)
    // that are far enough away to warrant the higher delivery fee. Everything
    // else pays the standard rate. Matched against the customer's postcode
    // with spaces stripped, so e.g. "WD23 3" matches any "WD23 3xx" postcode.
    private static final List<String> HIGHER_FEE_POSTCODE_PREFIXES = List.of(
            "WD233", "WD231", "WD234",
            "WD194", "WD196", "WD197",
            "WD33", "WD31",
            "AL2",
            "WD50",
            "WD4"
    );

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
                request.deliveryPostcode()
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

        BigDecimal deliveryFee = calculateDeliveryFee(request.orderType(), request.deliveryPostcode());
        order.setDeliveryFee(deliveryFee);
        order.setTotalPrice(subtotal.add(deliveryFee));

        return orderRepository.save(order);
    }

    private BigDecimal calculateDeliveryFee(OrderType orderType, String postcode) {
        if (orderType != OrderType.DELIVERY) {
            return BigDecimal.ZERO;
        }
        if (postcode == null || postcode.isBlank()) {
            throw new IllegalArgumentException("deliveryPostcode is required for delivery orders");
        }

        String normalized = postcode.toUpperCase().replace(" ", "");
        boolean isHigherFeeArea = HIGHER_FEE_POSTCODE_PREFIXES.stream().anyMatch(normalized::startsWith);

        return isHigherFeeArea ? HIGHER_DELIVERY_FEE : STANDARD_DELIVERY_FEE;
    }
}
