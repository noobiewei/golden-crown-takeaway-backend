package com.goldencrown.takeaway_backend.menu;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "menu_items")
public class MenuItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    private MenuCategory category;

    private boolean available = true;

    private boolean vegetarian = false;
    private boolean spicy = false;
    private boolean containsNuts = false;

    protected MenuItem() {}

    public MenuItem(String name, String description, BigDecimal price, MenuCategory category) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public BigDecimal getPrice() { return price; }
    public MenuCategory getCategory() { return category; }
    public boolean isAvailable() { return available; }
    public boolean isVegetarian() { return vegetarian; }
    public boolean isSpicy() { return spicy; }
    public boolean isContainsNuts() { return containsNuts; }

    public void setName(String name) { this.name = name; }
    public void setDescription(String description) { this.description = description; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public void setCategory(MenuCategory category) { this.category = category; }
    public void setAvailable(boolean available) { this.available = available; }
    public void setVegetarian(boolean vegetarian) { this.vegetarian = vegetarian; }
    public void setSpicy(boolean spicy) { this.spicy = spicy; }
    public void setContainsNuts(boolean containsNuts) { this.containsNuts = containsNuts; }
}
