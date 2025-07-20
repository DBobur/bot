package uz.app.service.repository;

import uz.app.service.model.Product;
import uz.app.service.model.enums.QuantityUnit;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProductRepository {

    private final Connection connection;

    public ProductRepository(Connection connection) {
        this.connection = connection;
    }

    // 🔹 Create
    public void save(Product product) {
        String sql = "INSERT INTO products(user_id, category_id, name, description, image, price, quantity_unit, quantity) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, product.getUserId());
            ps.setLong(2, product.getCategoryId());
            ps.setString(3, product.getName());
            ps.setString(4, product.getDescription());
            ps.setString(5, product.getImage());
            ps.setDouble(6, product.getPrice());
            ps.setString(7, product.getQuantityUnit().name());
            ps.setDouble(8, product.getQuantity());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error saving product", e);
        }
    }

    // 🔹 Update
    public void update(Product product) {
        String sql = "UPDATE products SET user_id = ?, category_id = ?, name = ?, description = ?, image = ?, price = ?, quantity_unit = ? WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, product.getUserId());
            ps.setLong(2, product.getCategoryId());
            ps.setString(3, product.getName());
            ps.setString(4, product.getDescription());
            ps.setString(5, product.getImage());
            ps.setDouble(6, product.getPrice());
            ps.setString(7, product.getQuantityUnit().name());
            ps.setLong(8, product.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating product", e);
        }
    }

    // 🔹 Delete
    public void deleteById(long id) {
        String sql = "DELETE FROM products WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting product", e);
        }
    }

    // 🔹 Find by ID
    public Optional<Product> findById(long id) {
        String sql = "SELECT * FROM products WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return Optional.of(mapResultSetToProduct(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding product by id", e);
        }
        return Optional.empty();
    }

    // 🔹 Find all
    public List<Product> findAll() {
        String sql = "SELECT * FROM products";
        List<Product> products = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                products.add(mapResultSetToProduct(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding all products", e);
        }
        return products;
    }

    // 🔹 Mapper
    private Product mapResultSetToProduct(ResultSet rs) throws SQLException {
        return Product.builder()
                .id(rs.getLong("id"))
                .userId(rs.getLong("user_id"))
                .categoryId(rs.getLong("category_id"))
                .name(rs.getString("name"))
                .description(rs.getString("description"))
                .image(rs.getString("image"))
                .price(rs.getDouble("price"))
                .quantityUnit(QuantityUnit.valueOf(rs.getString("quantity_unit")))
                .quantity(rs.getDouble("quantity"))
                .build();
    }
}