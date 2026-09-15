package com.goldencrown.takeaway_backend.order;

import com.goldencrown.takeaway_backend.menu.DishExtra;
import com.goldencrown.takeaway_backend.menu.ExtrasCatalog;
import com.goldencrown.takeaway_backend.menu.MenuItem;
import com.goldencrown.takeaway_backend.menu.MenuItemRepository;
import com.goldencrown.takeaway_backend.push.PushNotificationService;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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
    private final PushNotificationService pushNotificationService;

    public OrderController(
            OrderRepository orderRepository,
            MenuItemRepository menuItemRepository,
            PushNotificationService pushNotificationService) {
        this.orderRepository = orderRepository;
        this.menuItemRepository = menuItemRepository;
        this.pushNotificationService = pushNotificationService;
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

            OrderItem orderItem = new OrderItem(menuItem, line.quantity(), line.note());

            // Extras are only ever priced from the dish's own catalog entry,
            // never from the client, so a tampered request can't claim a
            // cheaper (or free) extra.
            List<DishExtra> available = ExtrasCatalog.forDish(menuItem.getName());
            if (line.extraNames() != null) {
                for (String extraName : line.extraNames()) {
                    available.stream()
                            .filter(extra -> extra.name().equals(extraName))
                            .findFirst()
                            .ifPresent(extra -> orderItem.addExtra(new OrderItemExtra(extra.name(), extra.nameZh(), extra.price())));
                }
            }

            order.addItem(orderItem);
            subtotal = subtotal.add(orderItem.getUnitPriceWithExtras().multiply(BigDecimal.valueOf(line.quantity())));
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
        pushNotificationService.notifyNewOrder(order.getId(), order.getCustomerName());

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
            String name = item.getMenuItem().getName();
            if (!item.getExtras().isEmpty()) {
                String extraNames = item.getExtras().stream()
                        .map(OrderItemExtra::getName)
                        .collect(Collectors.joining(", "));
                name = name + " + " + extraNames;
            }

            paramsBuilder.addLineItem(
                    SessionCreateParams.LineItem.builder()
                            .setQuantity((long) item.getQuantity())
                            .setPriceData(
                                    SessionCreateParams.LineItem.PriceData.builder()
                                            .setCurrency("gbp")
                                            .setUnitAmount(toPence(item.getUnitPriceWithExtras()))
                                            .setProductData(
                                                    SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                            .setName(name)
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
