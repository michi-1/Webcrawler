package org.example.webcrawlerui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.WebCrawler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class WebCrawlerUI extends Application {

    private TextField brandField = new TextField();
    private TextField modelField = new TextField();
    private TextField yearField = new TextField();
    private TextField priceField = new TextField();
    private Label statusLabel = new Label("Status: Ready");

    private String loggedInUserEmail; // Set this when the user logs in
    private static final String JDBC_URL = "jdbc:h2:./users"; // Your Database URL

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Carplatform Crawler");

        // Create UI components
        Label titleLabel = new Label("Carplatform Crawler");
        titleLabel.setStyle("-fx-font-size: 20; -fx-font-weight: bold;");

        Label brandLabel = new Label("Brand:");
        Label modelLabel = new Label("Model:");
        Label yearLabel = new Label("Year:");
        Label priceLabel = new Label("Price:");

        Button crawlButton = new Button("Crawl Website");
        crawlButton.setStyle("-fx-font-size: 14; -fx-font-weight: bold;");
        crawlButton.setOnAction(e -> {
            createSearchCriteriaTableIfNotExist();
            crawlWebsite();
        });

        Button deleteButton = new Button("Delete Criteria");
        deleteButton.setOnAction(e -> deleteSearchCriteria());

        Button exitButton = new Button("Exit");
        exitButton.setStyle("-fx-font-size: 14; -fx-font-weight: bold;");
        exitButton.setOnAction(e -> Platform.exit());

        // Layout
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 20, 20, 20));

        grid.add(titleLabel, 0, 0, 2, 1);
        grid.addRow(1, brandLabel, brandField);
        grid.addRow(2, modelLabel, modelField);
        grid.addRow(3, yearLabel, yearField);
        grid.addRow(4, priceLabel, priceField);

        HBox buttonBox = new HBox(10, crawlButton, deleteButton, exitButton);
        buttonBox.setAlignment(javafx.geometry.Pos.CENTER);

        VBox vbox = new VBox(grid, buttonBox, statusLabel);

        Scene scene = new Scene(vbox, 400, 250);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void createSearchCriteriaTableIfNotExist() {
        String sql = "CREATE TABLE IF NOT EXISTS search_criteria (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "user_email VARCHAR(255), " +
                "brand VARCHAR(255), " +
                "model VARCHAR(255), " +
                "\"year\" VARCHAR(255), " +  // "year" ist jetzt in Anführungszeichen
                "price VARCHAR(255), " +
                "FOREIGN KEY (user_email) REFERENCES users(email));"; // Stellt sicher, dass die users-Tabelle existiert und email enthält
        try (Connection connection = DriverManager.getConnection(JDBC_URL, "sa", "");
             Statement statement = connection.createStatement()) {
            statement.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void crawlWebsite() {
        String brand = brandField.getText();
        String model = modelField.getText();
        String year = yearField.getText();
        String price = priceField.getText();

        WebCrawler webCrawler = new WebCrawler(statusLabel);
        webCrawler.crawl(brand, model, year, price);

        saveSearchCriteria(brand, model, year, price);
    }

    private void saveSearchCriteria(String brand, String model, String year, String price) {
        String sql = "INSERT INTO search_criteria (user_email, brand, model, \"year\", price) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = DriverManager.getConnection(JDBC_URL, "sa", "");
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, loggedInUserEmail);
            statement.setString(2, brand);
            statement.setString(3, model);
            statement.setString(4, year);
            statement.setString(5, price);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private void deleteSearchCriteria() {
        String sql = "DELETE FROM search_criteria WHERE user_email = ?";
        try (Connection connection = DriverManager.getConnection(JDBC_URL, "sa", "");
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, loggedInUserEmail);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setLoggedInUserEmail(String email) {
        this.loggedInUserEmail = email;
    }

}
