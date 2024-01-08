package org.example.webcrawlerui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import java.sql.*;

public class LoginRegistrationUI extends Application {

    // JDBC connection URL for H2
    private static final String JDBC_URL = "jdbc:h2:./users";

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
        Label emailLabel = new Label("Email:");
        Label passwordLabel = new Label("Password:");

        // TextFields
        TextField emailTextField = new TextField();
        PasswordField passwordField = new PasswordField();

        // Buttons
        Button loginButton = new Button("Login");
        Button registerButton = new Button("Register");

        // Adding elements to the grid
        grid.add(emailLabel, 0, 0);
        grid.add(emailTextField, 1, 0);
        grid.add(passwordLabel, 0, 1);
        grid.add(passwordField, 1, 1);
        grid.add(loginButton, 0, 2);
        grid.add(registerButton, 1, 2);

        // Event handling for the buttons
        loginButton.setOnAction(e -> {
            if (isValidEmail(emailTextField.getText())) {
                handleLogin(emailTextField.getText(), passwordField.getText(), primaryStage);
            } else {
                showAlert("Invalid Email", "Please enter a valid email address.");
            }
        });
        registerButton.setOnAction(e -> {
            if (isValidEmail(emailTextField.getText())) {
                handleRegister(emailTextField.getText(), passwordField.getText());
            } else {
                showAlert("Invalid Email", "Please enter a valid email address.");
            }
        });

        Scene scene = new Scene(grid, 300, 200);
        primaryStage.setScene(scene);

        // Initialize the database (create the 'users' table if not exists)
        initializeDatabase();

        primaryStage.show();
    }

    private void initializeDatabase() {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, "sa", "");
             Statement statement = connection.createStatement()) {
            // UNIQUE Constraint für email hinzugefügt
            statement.execute("CREATE TABLE IF NOT EXISTS users (id INT AUTO_INCREMENT PRIMARY KEY, email VARCHAR(255) UNIQUE NOT NULL, password VARCHAR(255));");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private void handleLogin(String email, String password, Stage primaryStage) {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, "sa", "");
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM users WHERE email=? AND password=?")) {
            statement.setString(1, email);
            statement.setString(2, password);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                System.out.println("Login successful");
                System.out.println("Welcome!");
                WebCrawlerUI webCrawlerUI = new WebCrawlerUI();
                webCrawlerUI.setLoggedInUserEmail(email);
                primaryStage.close();
                openWebCrawlerUI(email);

            } else {
                showAlert("Login Failed", "Invalid email or password.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void handleRegister(String email, String password) {
        if (userExists(email)) {
            showAlert("Registration Failed", "Email already exists.");
        } else {
            try (Connection connection = DriverManager.getConnection(JDBC_URL, "sa", "");
                 PreparedStatement statement = connection.prepareStatement("INSERT INTO users (email, password) VALUES (?, ?)")) {
                statement.setString(1, email);
                statement.setString(2, password);

                int rowsInserted = statement.executeUpdate();
                if (rowsInserted > 0) {
                    showAlert("Registration Successful", "Your Registration was successful!");
                } else {
                    showAlert("Registration Failed", "An error occurred. Please try again.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean userExists(String email) {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, "sa", "");
             PreparedStatement statement = connection.prepareStatement("SELECT COUNT(*) FROM users WHERE email=?")) {
            statement.setString(1, email);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void openWebCrawlerUI(String email) {
        WebCrawlerUI webCrawlerUI = new WebCrawlerUI();
        webCrawlerUI.setLoggedInUserEmail(email); // Set the email before showing the UI
        Stage webCrawlerStage = new Stage();
        webCrawlerUI.start(webCrawlerStage);
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(emailRegex);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}