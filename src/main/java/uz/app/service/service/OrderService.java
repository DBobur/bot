package uz.app.service.service;

import uz.app.service.model.Order;
import uz.app.service.repository.OrderRepository;

import java.util.List;

public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    // 🔹 Create
    public void saveOrder(Order order) {
        orderRepository.save(order);
    }

    // 🔹 Update
    public void updateOrder(Order order) {
        if (!orderRepository.findById(order.getId()).isPresent()) {
            throw new RuntimeException("Order not found with id: " + order.getId());
        }
        orderRepository.update(order);
    }

    // 🔹 Delete
    public void deleteOrder(long id) {
        if (!orderRepository.findById(id).isPresent()) {
            throw new RuntimeException("Order not found with id: " + id);
        }
        orderRepository.deleteById(id);
    }

    // 🔹 Get by ID
    public Order getOrderById(long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));
    }

    // 🔹 Get all
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }
}