package com.goldencrown.takeaway_backend.order;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String customerName;
    private String customerPhone;

    @Enumerated(EnumType.STRING)
    private OrderType orderType;

    private String deliveryAddress;
    private String deliveryPostcode;

    @Column(length = 1000)
    private String specialInstructions;

    // nvarchar: see MenuItem.nameZh for why plain varchar can't hold Chinese.
    @Column(columnDefinition = "nvarchar(1000)")
    private String specialInstructionsZh;

    private BigDecimal deliveryFee = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    private OrderStatus status = OrderStatus.PENDING;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus = PaymentStatus.UNPAID;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    private String stripeSessionId;
    private String orderToken;

    @Enumerated(EnumType.STRING)
    private FreeDrinkChoice freeDrinkChoice;

    private LocalDateTime createdAt = LocalDateTime.now();

    private BigDecimal totalPrice = BigDecimal.ZERO;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items = new ArrayList<>();

    protected Order() {}

    public Order(String customerName, String customerPhone, OrderType orderType, String deliveryAddress, String deliveryPostcode, String specialInstructions, PaymentMethod paymentMethod) {
        this.customerName = customerName;
        this.customerPhone = customerPhone;
        this.orderType = orderType;
        this.deliveryAddress = deliveryAddress;
        this.deliveryPostcode = deliveryPostcode;
        this.specialInstructions = specialInstructions;
        this.paymentMethod = paymentMethod;
    }

    public void addItem(OrderItem item) {
        items.add(item);
        item.setOrder(this);
    }

    public Long getId() { return id; }
    public String getCustomerName() { return customerName; }
    public String getCustomerPhone() { return customerPhone; }
    public OrderType getOrderType() { return orderType; }
    public String getDeliveryAddress() { return deliveryAddress; }
    public String getDeliveryPostcode() { return deliveryPostcode; }
    public String getSpecialInstructions() { return specialInstructions; }
    public String getSpecialInstructionsZh() { return specialInstructionsZh; }
    public BigDecimal getDeliveryFee() { return deliveryFee; }
    public OrderStatus getStatus() { return status; }
    public PaymentStatus getPaymentStatus() { return paymentStatus; }
    public PaymentMethod getPaymentMethod() { return paymentMethod; }
    public String getStripeSessionId() { return stripeSessionId; }
    public String getOrderToken() { return orderToken; }
    public FreeDrinkChoice getFreeDrinkChoice() { return freeDrinkChoice; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public BigDecimal getTotalPrice() { return totalPrice; }
    public List<OrderItem> getItems() { return items; }

    public void setDeliveryFee(BigDecimal deliveryFee) { this.deliveryFee = deliveryFee; }
    public void setTotalPrice(BigDecimal totalPrice) { this.totalPrice = totalPrice; }
    public void setStatus(OrderStatus status) { this.status = status; }
    public void setPaymentStatus(PaymentStatus paymentStatus) { this.paymentStatus = paymentStatus; }
    public void setStripeSessionId(String stripeSessionId) { this.stripeSessionId = stripeSessionId; }
    public void setOrderToken(String orderToken) { this.orderToken = orderToken; }
    public void setFreeDrinkChoice(FreeDrinkChoice freeDrinkChoice) { this.freeDrinkChoice = freeDrinkChoice; }
    public void setSpecialInstructionsZh(String specialInstructionsZh) { this.specialInstructionsZh = specialInstructionsZh; }
}
