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

    // nvarchar: see MenuItem.nameZh for why plain varchar can't hold Chinese.
    @Column(columnDefinition = "nvarchar(255)")
    private String nameZh;

    private BigDecimal priceAtOrder;

    protected OrderItemExtra() {}

    public OrderItemExtra(String name, String nameZh, BigDecimal priceAtOrder) {
        this.name = name;
        this.nameZh = nameZh;
        this.priceAtOrder = priceAtOrder;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getNameZh() { return nameZh; }
    public BigDecimal getPriceAtOrder() { return priceAtOrder; }

    void setOrderItem(OrderItem orderItem) { this.orderItem = orderItem; }
}
