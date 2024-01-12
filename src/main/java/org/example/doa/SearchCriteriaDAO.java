package org.example.doa;

import org.example.model.SearchCriteria;
import org.example.util.AppConfig;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SearchCriteriaDAO {


    public SearchCriteriaDAO() {
        createSearchCriteriaTableIfNotExist();
    }

    private void createSearchCriteriaTableIfNotExist() {
        String sql = "CREATE TABLE IF NOT EXISTS search_criteria (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "user_email VARCHAR(255) UNIQUE, " +
                "brand VARCHAR(255), " +
                "model VARCHAR(255), " +
                "\"year\" VARCHAR(255), " +
                "price VARCHAR(255), " +
                "FOREIGN KEY (user_email) REFERENCES users(email));";
        try (Connection connection = DriverManager.getConnection(AppConfig.database_URL, "sa", "");
             Statement statement = connection.createStatement()) {
            statement.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveSearchCriteria(String userEmail, String brand, String model, String year, String price) throws SQLException {
        if (!userHasCriteria(userEmail)) {
            String sql = "INSERT INTO search_criteria (user_email, brand, model, \"year\", price) VALUES (?, ?, ?, ?, ?)";
            try (Connection connection = DriverManager.getConnection(AppConfig.database_URL, "sa", "");
                 PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, userEmail);
                statement.setString(2, brand);
                statement.setString(3, model);
                statement.setString(4, year);
                statement.setString(5, price);
                statement.executeUpdate();
            }
        }
    }

    public void deleteSearchCriteria(String userEmail) throws SQLException {
        String sql = "DELETE FROM search_criteria WHERE user_email = ?";
        try (Connection connection = DriverManager.getConnection(AppConfig.database_URL, "sa", "");
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, userEmail);
            statement.executeUpdate();
        }
    }

    public boolean userHasCriteria(String userEmail) throws SQLException {
        String sql = "SELECT COUNT(*) FROM search_criteria WHERE user_email = ?";
        try (Connection connection = DriverManager.getConnection(AppConfig.database_URL, "sa", "");
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, userEmail);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1) > 0;
                }
            }
        }
        return false;
    }
    public List<SearchCriteria> getAllSearchCriteria() throws SQLException {
        List<SearchCriteria> allCriteria = new ArrayList<>();
        String sql = "SELECT * FROM search_criteria";
        try (Connection connection = DriverManager.getConnection(AppConfig.database_URL, "sa", "");
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                SearchCriteria criteria = new SearchCriteria(
                        resultSet.getInt("id"),
                        resultSet.getString("user_email"),
                        resultSet.getString("brand"),
                        resultSet.getString("model"),
                        resultSet.getString("year"),
                        resultSet.getString("price")
                );
                allCriteria.add(criteria);
            }
        }
        return allCriteria;
    }
}
