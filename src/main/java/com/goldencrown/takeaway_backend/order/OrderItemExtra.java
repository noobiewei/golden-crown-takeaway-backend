package com.goldencrown.takeaway_backend.order;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "order_item_extras")
public class OrderItemExtra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_item_id")
    @JsonIgnore
    private OrderItem orderItem;

    private String name;
    private BigDecimal priceAtOrder;

    protected OrderItemExtra() {}

    public OrderItemExtra(String name, BigDecimal priceAtOrder) {
        this.name = name;
        this.priceAtOrder = priceAtOrder;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public BigDecimal getPriceAtOrder() { return priceAtOrder; }

    void setOrderItem(OrderItem orderItem) { this.orderItem = orderItem; }
}
