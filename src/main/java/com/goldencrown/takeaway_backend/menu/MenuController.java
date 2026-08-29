package com.goldencrown.takeaway_backend.menu;

import com.goldencrown.takeaway_backend.order.OrderItemRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/menu")
public class MenuController {

    private final MenuItemRepository menuItemRepository;
    private final OrderItemRepository orderItemRepository;

    public MenuController(MenuItemRepository menuItemRepository, OrderItemRepository orderItemRepository) {
        this.menuItemRepository = menuItemRepository;
        this.orderItemRepository = orderItemRepository;
    }

    @GetMapping
    public List<MenuItem> getMenu() {
        return menuItemRepository.findAll();
    }

    @GetMapping("/popular")
    public List<MenuItem> getPopularItems() {
        return orderItemRepository.findPopularMenuItems(PageRequest.of(0, 4));
    }

    @GetMapping("/extras-catalog")
    public Map<String, List<DishExtra>> getExtrasCatalog() {
        return ExtrasCatalog.all();
    }
}
