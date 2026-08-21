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
                request.deliveryAddress()
        );

        BigDecimal total = BigDecimal.ZERO;
        for (OrderLineRequest line : request.items()) {
            MenuItem menuItem = menuItemRepository.findById(line.menuItemId())
                    .orElseThrow(() -> new IllegalArgumentException("Menu item not found: " + line.menuItemId()));

            order.addItem(new OrderItem(menuItem, line.quantity()));
            total = total.add(menuItem.getPrice().multiply(BigDecimal.valueOf(line.quantity())));
        }
        order.setTotalPrice(total);

        return orderRepository.save(order);
    }
}
