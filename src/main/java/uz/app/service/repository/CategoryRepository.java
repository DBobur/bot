package uz.app.service.repository;
import uz.app.service.model.Category;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CategoryRepository {

    private final Connection connection;

    public CategoryRepository(Connection connection) {
        this.connection = connection;
    }

    // 🔹 Create
    public void save(Category category) {
        String sql = "INSERT INTO categories(name, description, created_by_user_id) VALUES (?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, category.getName());
            ps.setString(2, category.getDescription());
            ps.setLong(3, category.getCreatedByUserId()); // ✅ foydalanuvchi ID
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error saving category", e);
        }
    }

    // 🔹 Update
    public void update(Category category) {
        String sql = "UPDATE categories SET name = ?, description = ? WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, category.getName());
            ps.setString(2, category.getDescription());
            ps.setLong(3, category.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating category", e);
        }
    }

    // 🔹 Delete
    public void deleteById(long id) {
        String sql = "DELETE FROM categories WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting category", e);
        }
    }

    // 🔹 Find by ID
    public Optional<Category> findById(long id) {
        String sql = "SELECT * FROM categories WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return Optional.of(mapResultSetToCategory(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding category by id", e);
        }
        return Optional.empty();
    }

    // 🔹 Find all
    public List<Category> findAll() {
        String sql = "SELECT * FROM categories";
        List<Category> categories = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                categories.add(mapResultSetToCategory(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding all categories", e);
        }
        return categories;
    }

    // 🔹 Mapper
    private Category mapResultSetToCategory(ResultSet rs) throws SQLException {
        return Category.builder()
                .id(rs.getLong("id"))
                .name(rs.getString("name"))
                .description(rs.getString("description"))
                .createdByUserId(rs.getLong("created_by_user_id")) // ✅ qo‘shuvchi user ID
                .build();
    }

    // 🔹 Find by User ID
    public List<Category> findByUserId(long userId) {
        String sql = "SELECT * FROM categories WHERE created_by_user_id = ?";
        List<Category> categories = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                categories.add(mapResultSetToCategory(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding categories by userId", e);
        }
        return categories;
    }

    // 🔹 Exists by ID
    public boolean existsById(long id) {
        String sql = "SELECT COUNT(*) FROM categories WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error checking existence of category by id", e);
        }
        return false;
    }

}
