package uz.app.service.service;

import uz.app.service.model.OrderItem;
import uz.app.service.repository.OrderItemRepository;

import java.sql.SQLException;
import java.util.List;

public class OrderItemService {

    private final OrderItemRepository orderItemRepository;

    public OrderItemService(OrderItemRepository orderItemRepository) {
        this.orderItemRepository = orderItemRepository;
    }

    // 🔹 Save one item
    public void saveOrderItem(OrderItem item) {
        try {
            orderItemRepository.save(item);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to save order item", e);
        }
    }

    // 🔹 Save list of items
    public void saveOrderItems(List<OrderItem> items) {
        for (OrderItem item : items) {
            saveOrderItem(item);
        }
    }

    // 🔹 Get items by order ID
    public List<OrderItem> getItemsByOrderId(long orderId) {
        try {
            return orderItemRepository.findByOrderId(orderId);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get items for order ID: " + orderId, e);
        }
    }

    // 🔹 Delete all items by order ID
    public void deleteItemsByOrderId(long orderId) {
        try {
            orderItemRepository.deleteByOrderId(orderId);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete items for order ID: " + orderId, e);
        }
    }
}