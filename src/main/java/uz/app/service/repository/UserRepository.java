package uz.app.service.repository;

import uz.app.service.model.User;
import uz.app.service.model.enums.UserRole;
import uz.app.service.model.enums.UserState;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserRepository{
    private final Connection connection;

    public UserRepository(Connection connection) {
        this.connection = connection;
    }

    public void save(User user) {
        String sql = "INSERT INTO users (chat_id, first_name, last_name, bio, number, role, user_state) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, user.getChatId());
            ps.setString(2, user.getFirstName());
            ps.setString(3, user.getLastName());
            ps.setString(4, user.getBio());
            ps.setString(5, user.getNumber());
            ps.setString(6, user.getRole().name());
            ps.setString(7, user.getUserState().name());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error saving user", e);
        }
    }

    public void update(User user) {
        String sql = "UPDATE users SET chat_id = ?, first_name = ?, last_name = ?, bio = ?, number = ?, role = ?, user_state = ? WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, user.getChatId());
            ps.setString(2, user.getFirstName());
            ps.setString(3, user.getLastName());
            ps.setString(4, user.getBio());
            ps.setString(5, user.getNumber());
            ps.setString(6, user.getRole().name());
            ps.setString(7, user.getUserState().name());
            ps.setLong(8, user.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating user", e);
        }
    }

    public void deleteById(long id) {
        String sql = "DELETE FROM users WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting user", e);
        }
    }

    public Optional<User> findById(long id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return Optional.of(mapResultSetToUser(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding user by id", e);
        }
        return Optional.empty();
    }

    public List<User> findAll() {
        String sql = "SELECT * FROM users";
        List<User> users = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                users.add(mapResultSetToUser(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding all users", e);
        }
        return users;
    }

    public Optional<User> findByChatId(long chatId) {
        String sql = "SELECT * FROM users WHERE chat_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, chatId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return Optional.of(mapResultSetToUser(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding user by chatId", e);
        }
        return Optional.empty();
    }

    private User mapResultSetToUser(ResultSet rs) throws SQLException {
        return User.builder()
                .id(rs.getLong("id"))
                .chatId(rs.getLong("chat_id"))
                .firstName(rs.getString("first_name"))
                .lastName(rs.getString("last_name"))
                .bio(rs.getString("bio"))
                .number(rs.getString("number"))
                .role(UserRole.valueOf(rs.getString("role")))
                .userState(UserState.valueOf(rs.getString("user_state")))
                .build();
    }
}

