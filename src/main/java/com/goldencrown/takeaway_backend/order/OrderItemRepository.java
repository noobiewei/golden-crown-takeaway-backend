package com.goldencrown.takeaway_backend.order;

import com.goldencrown.takeaway_backend.menu.MenuItem;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    @Query("SELECT oi.menuItem FROM OrderItem oi GROUP BY oi.menuItem ORDER BY SUM(oi.quantity) DESC")
    List<MenuItem> findPopularMenuItems(Pageable pageable);
}
