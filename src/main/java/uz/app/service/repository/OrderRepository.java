package uz.app.service.repository;

import uz.app.service.model.Order;
import uz.app.service.model.enums.OrderStatus;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OrderRepository {

    private final Connection connection;

    public OrderRepository(Connection connection) {
        this.connection = connection;
    }

    // 🔹 Create
    public void save(Order order) {
        String sql = "INSERT INTO orders(user_id, order_status) VALUES (?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, order.getUserId());
            ps.setString(2, order.getOrderStatus().name());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error saving order", e);
        }
    }

    // 🔹 Update
    public void update(Order order) {
        String sql = "UPDATE orders SET user_id = ?, order_status = ? WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, order.getUserId());
            ps.setString(2, order.getOrderStatus().name());
            ps.setLong(3, order.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating order", e);
        }
    }

    // 🔹 Delete
    public void deleteById(long id) {
        String sql = "DELETE FROM orders WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting order", e);
        }
    }

    // 🔹 Find by ID
    public Optional<Order> findById(long id) {
        String sql = "SELECT * FROM orders WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return Optional.of(mapResultSetToOrder(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding order by id", e);
        }
        return Optional.empty();
    }

    // 🔹 Find all
    public List<Order> findAll() {
        String sql = "SELECT * FROM orders";
        List<Order> orders = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                orders.add(mapResultSetToOrder(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding all orders", e);
        }
        return orders;
    }

    // 🔹 Mapper
    private Order mapResultSetToOrder(ResultSet rs) throws SQLException {
        return Order.builder()
                .id(rs.getLong("id"))
                .userId(rs.getLong("user_id"))
                .orderStatus(OrderStatus.valueOf(rs.getString("order_status")))
                .build();
    }
}