package org.example.webcrawlerui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LoginRegistrationUI extends Application {

    // JDBC connection URL for H2
    private static final String JDBC_URL = "jdbc:h2:./users";

    private List<User> users = new ArrayList<>();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Login & Registration");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 20, 20, 20));

        // Labels
        Label usernameLabel = new Label("Username:");
        Label passwordLabel = new Label("Password:");

        // TextFields
        TextField usernameTextField = new TextField();
        PasswordField passwordField = new PasswordField();

        // Buttons
        Button loginButton = new Button("Login");
        Button registerButton = new Button("Register");

        // Adding elements to the grid
        grid.add(usernameLabel, 0, 0);
        grid.add(usernameTextField, 1, 0);
        grid.add(passwordLabel, 0, 1);
        grid.add(passwordField, 1, 1);
        grid.add(loginButton, 0, 2);
        grid.add(registerButton, 1, 2);

        // Event handling for the buttons
        loginButton.setOnAction(e -> handleLogin(usernameTextField.getText(), passwordField.getText(), primaryStage));
        registerButton.setOnAction(e -> handleRegister(usernameTextField.getText(), passwordField.getText()));

        Scene scene = new Scene(grid, 300, 200);
        primaryStage.setScene(scene);

        // Initialize the database (create the 'users' table if not exists)
        initializeDatabase();

        primaryStage.show();
    }

    private void initializeDatabase() {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, "sa", "");
             PreparedStatement statement = connection.prepareStatement(
                     "CREATE TABLE IF NOT EXISTS users (id INT AUTO_INCREMENT PRIMARY KEY, username VARCHAR(255), password VARCHAR(255));")) {
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void handleLogin(String username, String password, Stage primaryStage) {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, "sa", "");
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM users WHERE username=? AND password=?")) {
            statement.setString(1, username);
            statement.setString(2, password);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                System.out.println("Login successful");
                System.out.println("Welcome, " + username + "!");
                primaryStage.close();
                openWebCrawlerUI();
            } else {
                System.out.println("Login failed. Invalid username or password.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void handleRegister(String username, String password) {
        if (userExists(username)) {
            System.out.println("Registration failed. Username already exists.");
        } else {
            try (Connection connection = DriverManager.getConnection(JDBC_URL, "sa", "");
                 PreparedStatement statement = connection.prepareStatement("INSERT INTO users (username, password) VALUES (?, ?)")) {
                statement.setString(1, username);
                statement.setString(2, password);

                int rowsInserted = statement.executeUpdate();
                if (rowsInserted > 0) {
                    System.out.println("Registration successful. Welcome, " + username + "!");
                } else {
                    System.out.println("Registration failed.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    private boolean userExists(String username) {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, "sa", "");
             PreparedStatement statement = connection.prepareStatement("SELECT COUNT(*) FROM users WHERE username=?")) {
            statement.setString(1, username);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                return count > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }


    private void openWebCrawlerUI() {
        WebCrawlerUI webCrawlerUI = new WebCrawlerUI();
        Stage webCrawlerStage = new Stage();
        webCrawlerUI.start(webCrawlerStage);
    }

    private static class User {
        private String username;
        private String password;

        public User(String username, String password) {
            this.username = username;
            this.password = password;
        }
    }
}
