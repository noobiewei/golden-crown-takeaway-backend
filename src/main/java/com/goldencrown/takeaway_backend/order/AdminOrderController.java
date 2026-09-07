package com.goldencrown.takeaway_backend.order;

import com.goldencrown.takeaway_backend.assistant.TranslationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/orders")
public class AdminOrderController {

    private final OrderRepository orderRepository;
    private final TranslationService translationService;

    public AdminOrderController(OrderRepository orderRepository, TranslationService translationService) {
        this.orderRepository = orderRepository;
        this.translationService = translationService;
    }

    @GetMapping
    public List<Order> getAllOrders() {
        return orderRepository.findAllByOrderByCreatedAtDesc();
    }

    @PatchMapping("/{id}/status")
    public Order updateStatus(@PathVariable Long id, @RequestBody UpdateOrderStatusRequest request) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Order not found: " + id));

        order.setStatus(request.status());
        return orderRepository.save(order);
    }

    // Dish and extra names are translated ahead of time via the menu/extras
    // catalogs, but free-text notes are one-off per order — translate them
    // here on first request and cache the result so reprinting the same
    // receipt doesn't call the AI again.
    @GetMapping("/{id}/receipt")
    public Order getReceipt(@PathVariable Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Order not found: " + id));

        boolean changed = false;

        if (order.getSpecialInstructions() != null && !order.getSpecialInstructions().isBlank()
                && order.getSpecialInstructionsZh() == null) {
            order.setSpecialInstructionsZh(translationService.translateToChinese(order.getSpecialInstructions()));
            changed = true;
        }

        for (OrderItem item : order.getItems()) {
            if (item.getNote() != null && !item.getNote().isBlank() && item.getNoteZh() == null) {
                item.setNoteZh(translationService.translateToChinese(item.getNote()));
                changed = true;
            }
        }

        return changed ? orderRepository.save(order) : order;
    }
}
