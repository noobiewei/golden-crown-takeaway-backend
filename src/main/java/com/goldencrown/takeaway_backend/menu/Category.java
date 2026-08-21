package com.goldencrown.takeaway_backend.menu;

import jakarta.persistence.*;

@Entity
@Table(name = "categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    private int displayOrder;

    protected Category() {}

    public Category(String name, int displayOrder) {
        this.name = name;
        this.displayOrder = displayOrder;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public int getDisplayOrder() { return displayOrder; }

    public void setName(String name) { this.name = name; }
    public void setDisplayOrder(int displayOrder) { this.displayOrder = displayOrder; }
}
