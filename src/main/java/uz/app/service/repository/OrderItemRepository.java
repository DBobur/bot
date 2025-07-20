package uz.app.service.repository;

import uz.app.service.model.OrderItem;
import uz.app.service.model.enums.QuantityUnit;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderItemRepository {

    private final Connection connection;

    public OrderItemRepository(Connection connection) {
        this.connection = connection;
    }

    public void save(OrderItem item) throws SQLException {
        String sql = "INSERT INTO order_items(order_id, product_id, quantity, quantity_unit) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, item.getOrderId());
            ps.setLong(2, item.getProductId());
            ps.setLong(3, item.getQuantity());
            ps.setString(4, item.getQuantityUnit().name());
            ps.executeUpdate();
        }
    }

    public List<OrderItem> findByOrderId(long orderId) throws SQLException {
        String sql = "SELECT * FROM order_items WHERE order_id = ?";
        List<OrderItem> items = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, orderId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                items.add(mapRow(rs));
            }
        }
        return items;
    }

    private OrderItem mapRow(ResultSet rs) throws SQLException {
        return OrderItem.builder()
                .id(rs.getLong("id"))
                .orderId(rs.getLong("order_id"))
                .productId(rs.getLong("product_id"))
                .quantity(rs.getLong("quantity"))
                .quantityUnit(QuantityUnit.valueOf(rs.getString("quantity_unit")))
                .build();
    }

    public void deleteByOrderId(long orderId) throws SQLException {
        String sql = "DELETE FROM order_items WHERE order_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, orderId);
            ps.executeUpdate();
        }
    }
}