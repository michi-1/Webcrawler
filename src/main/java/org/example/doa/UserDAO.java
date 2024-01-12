package org.example.doa;


import org.example.model.User;
import org.example.util.AppConfig;

import java.sql.*;

public class UserDAO {


    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(AppConfig.database_URL, "sa", "");
    }
    public UserDAO() {
        createUsersTableIfNotExists();
    }

    public User getUser(int userId) throws SQLException {
        String query = "SELECT * FROM users WHERE id = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new User(
                            resultSet.getInt("id"),
                            resultSet.getString("email"),
                            resultSet.getString("password")
                    );
                }
            }
        }
        return null;
    }

    public boolean createUser(String email, String password) throws SQLException {
        String query = "INSERT INTO users (email, password) VALUES (?, ?)";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, email);
            statement.setString(2, password);

            int rowsInserted = statement.executeUpdate();
            return rowsInserted > 0;
        }
    }

    public boolean deleteUser(int userId) throws SQLException {
        String query = "DELETE FROM users WHERE id = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userId);

            int rowsDeleted = statement.executeUpdate();
            return rowsDeleted > 0;
        }
    }

    public boolean userExists(String email) throws SQLException {
        String query = "SELECT COUNT(*) FROM users WHERE email = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, email);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1) > 0;
                }
            }
        }
        return false;
    }
    public User getUserByEmailAndPassword(String email, String password) throws SQLException {
        String query = "SELECT * FROM users WHERE email = ? AND password = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, email);
            statement.setString(2, password);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new User(
                            resultSet.getInt("id"),
                            resultSet.getString("email"),
                            resultSet.getString("password")
                    );
                }
            }
        }
        return null;
    }
    private void createUsersTableIfNotExists() {
        try (Connection connection = DriverManager.getConnection(AppConfig.database_URL, "sa", "");
             Statement statement = connection.createStatement()) {
            statement.execute("CREATE TABLE IF NOT EXISTS users (id INT AUTO_INCREMENT PRIMARY KEY, email VARCHAR(255) UNIQUE NOT NULL, password VARCHAR(255));");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public int getUserIdByEmail(String email) throws SQLException {
        String sql = "SELECT id FROM users WHERE email = ?";
        try (Connection connection = DriverManager.getConnection(AppConfig.database_URL, "sa", "");
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, email);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("id");
                } else {
                    return -1;
                }
            }
        }
    }
}

