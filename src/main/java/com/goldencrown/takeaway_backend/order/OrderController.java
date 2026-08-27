package com.goldencrown.takeaway_backend.order;

import com.goldencrown.takeaway_backend.menu.MenuItem;
import com.goldencrown.takeaway_backend.menu.MenuItemRepository;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private static final BigDecimal MINIMUM_DELIVERY_ORDER = new BigDecimal("15");
    private static final BigDecimal STANDARD_DELIVERY_FEE = new BigDecimal("1.30");
    private static final BigDecimal HIGHER_DELIVERY_FEE = new BigDecimal("3.00");
    private static final BigDecimal FREE_DRINK_THRESHOLD = new BigDecimal("55");

    @Value("${app.frontend-url}")
    private String frontendUrl;

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
    public CreateOrderResponse createOrder(@RequestBody CreateOrderRequest request) throws StripeException {
        if (request.customerName() == null || request.customerName().isBlank()) {
            throw new IllegalArgumentException("Name is required");
        }
        if (request.customerPhone() == null || request.customerPhone().isBlank()) {
            throw new IllegalArgumentException("Phone is required");
        }
        if (request.paymentMethod() == null) {
            throw new IllegalArgumentException("paymentMethod is required");
        }

        Order order = new Order(
                request.customerName().trim(),
                request.customerPhone().trim(),
                request.orderType(),
                request.deliveryAddress(),
                request.deliveryPostcode(),
                request.specialInstructions(),
                request.paymentMethod()
        );

        BigDecimal subtotal = BigDecimal.ZERO;
        for (OrderLineRequest line : request.items()) {
            MenuItem menuItem = menuItemRepository.findById(line.menuItemId())
                    .orElseThrow(() -> new IllegalArgumentException("Menu item not found: " + line.menuItemId()));

            order.addItem(new OrderItem(menuItem, line.quantity(), line.note()));
            subtotal = subtotal.add(menuItem.getPrice().multiply(BigDecimal.valueOf(line.quantity())));
        }

        if (request.orderType() == OrderType.DELIVERY && subtotal.compareTo(MINIMUM_DELIVERY_ORDER) < 0) {
            throw new IllegalArgumentException(
                    "Minimum order for delivery is £" + MINIMUM_DELIVERY_ORDER + " (subtotal was £" + subtotal + ")");
        }

        BigDecimal deliveryFee = calculateDeliveryFee(request.orderType(), request.deliveryPostcode());
        order.setDeliveryFee(deliveryFee);
        order.setTotalPrice(subtotal.add(deliveryFee));

        // Only honour a free-drink choice if the order actually qualifies —
        // checked server-side so it can't be claimed via a direct API call.
        if (request.freeDrinkChoice() != null && subtotal.compareTo(FREE_DRINK_THRESHOLD) >= 0) {
            order.setFreeDrinkChoice(request.freeDrinkChoice());
        }

        order = orderRepository.save(order);

        if (request.paymentMethod() == PaymentMethod.CASH) {
            order.setOrderToken(UUID.randomUUID().toString());
            order = orderRepository.save(order);
            return new CreateOrderResponse(order, null);
        }

        Session session = createCheckoutSession(order, deliveryFee);
        order.setStripeSessionId(session.getId());
        order = orderRepository.save(order);

        return new CreateOrderResponse(order, session.getUrl());
    }

    @GetMapping("/by-session/{sessionId}")
    public Order getBySessionId(@PathVariable String sessionId) {
        return orderRepository.findByStripeSessionId(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("No order for session: " + sessionId));
    }

    @GetMapping("/by-token/{orderToken}")
    public Order getByOrderToken(@PathVariable String orderToken) {
        return orderRepository.findByOrderToken(orderToken)
                .orElseThrow(() -> new IllegalArgumentException("No order for token: " + orderToken));
    }

    private Session createCheckoutSession(Order order, BigDecimal deliveryFee) throws StripeException {
        SessionCreateParams.Builder paramsBuilder = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl(frontendUrl + "/confirmation?session_id={CHECKOUT_SESSION_ID}")
                .setCancelUrl(frontendUrl + "/checkout")
                .putMetadata("orderId", order.getId().toString());

        for (OrderItem item : order.getItems()) {
            paramsBuilder.addLineItem(
                    SessionCreateParams.LineItem.builder()
                            .setQuantity((long) item.getQuantity())
                            .setPriceData(
                                    SessionCreateParams.LineItem.PriceData.builder()
                                            .setCurrency("gbp")
                                            .setUnitAmount(toPence(item.getPriceAtOrder()))
                                            .setProductData(
                                                    SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                            .setName(item.getMenuItem().getName())
                                                            .build()
                                            )
                                            .build()
                            )
                            .build()
            );
        }

        if (deliveryFee.compareTo(BigDecimal.ZERO) > 0) {
            paramsBuilder.addLineItem(
                    SessionCreateParams.LineItem.builder()
                            .setQuantity(1L)
                            .setPriceData(
                                    SessionCreateParams.LineItem.PriceData.builder()
                                            .setCurrency("gbp")
                                            .setUnitAmount(toPence(deliveryFee))
                                            .setProductData(
                                                    SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                            .setName("Delivery fee")
                                                            .build()
                                            )
                                            .build()
                            )
                            .build()
            );
        }

        return Session.create(paramsBuilder.build());
    }

    private long toPence(BigDecimal amount) {
        return amount.multiply(BigDecimal.valueOf(100)).setScale(0, RoundingMode.HALF_UP).longValueExact();
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
