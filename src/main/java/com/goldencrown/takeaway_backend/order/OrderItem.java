package com.goldencrown.takeaway_backend.order;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.goldencrown.takeaway_backend.menu.MenuItem;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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
    private String note;
    private String noteZh;

    @OneToMany(mappedBy = "orderItem", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItemExtra> extras = new ArrayList<>();

    protected OrderItem() {}

    public OrderItem(MenuItem menuItem, int quantity, String note) {
        this.menuItem = menuItem;
        this.quantity = quantity;
        this.priceAtOrder = menuItem.getPrice();
        this.note = note;
    }

    public Long getId() { return id; }
    public MenuItem getMenuItem() { return menuItem; }
    public int getQuantity() { return quantity; }
    public BigDecimal getPriceAtOrder() { return priceAtOrder; }
    public String getNote() { return note; }
    public String getNoteZh() { return noteZh; }
    public List<OrderItemExtra> getExtras() { return extras; }

    public void addExtra(OrderItemExtra extra) {
        extras.add(extra);
        extra.setOrderItem(this);
    }

    public void setNoteZh(String noteZh) { this.noteZh = noteZh; }

    public BigDecimal getUnitPriceWithExtras() {
        return extras.stream()
                .map(OrderItemExtra::getPriceAtOrder)
                .reduce(priceAtOrder, BigDecimal::add);
    }

    void setOrder(Order order) { this.order = order; }
}
