package com.goldencrown.takeaway_backend.order;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.goldencrown.takeaway_backend.menu.MenuItem;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "order_items")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    @JsonIgnore
    private Order order;

    @ManyToOne
    @JoinColumn(name = "menu_item_id")
    private MenuItem menuItem;

    private int quantity;
    private BigDecimal priceAtOrder;

    protected OrderItem() {}

    public OrderItem(MenuItem menuItem, int quantity) {
        this.menuItem = menuItem;
        this.quantity = quantity;
        this.priceAtOrder = menuItem.getPrice();
    }

    public Long getId() { return id; }
    public MenuItem getMenuItem() { return menuItem; }
    public int getQuantity() { return quantity; }
    public BigDecimal getPriceAtOrder() { return priceAtOrder; }

    void setOrder(Order order) { this.order = order; }
}
