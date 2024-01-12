package org.example.ui;


import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.core.WebCrawler;
import org.example.model.Car;
import org.example.model.SearchCriteria;
import org.example.services.SearchCriteriaService;
import org.example.services.UserService;

import java.util.List;


public class WebCrawlerUI extends Application {

    private TextField brandField = new TextField();
    private TextField modelField = new TextField();
    private TextField yearField = new TextField();
    private TextField priceField = new TextField();
    private Label statusLabel = new Label("Status: Ready");

    private String loggedInUserEmail;
    private SearchCriteriaService searchCriteriaService = new SearchCriteriaService();

    private UserService userService = new UserService();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Carplatform Crawler");

        Label titleLabel = new Label("Carplatform Crawler");
        titleLabel.setStyle("-fx-font-size: 20; -fx-font-weight: bold;");

        Label brandLabel = new Label("Brand:");
        Label modelLabel = new Label("Model:");
        Label yearLabel = new Label("Year:");
        Label priceLabel = new Label("Price:");

        Button saveButton = new Button("Save Criteria");
        saveButton.setOnAction(e -> saveOrUpdateSearchCriteria());

        Button deleteButton = new Button("Delete Criteria");
        deleteButton.setOnAction(e -> deleteSearchCriteria());
        Button deleteUser = new Button("Delete Account");
        deleteUser.setOnAction(e -> deleteAccount());

        Button logoutButton = new Button("Logout");
        logoutButton.setOnAction(e -> logoutUser());

        Button exitButton = new Button("Exit");
        exitButton.setOnAction(e -> Platform.exit());


        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 20, 20, 20));
        statusLabel.setPadding(new Insets(20, 0, 0, 10));

        grid.add(titleLabel, 0, 0, 2, 1);
        grid.addRow(1, brandLabel, brandField);
        grid.addRow(2, modelLabel, modelField);
        grid.addRow(3, yearLabel, yearField);
        grid.addRow(4, priceLabel, priceField);

        HBox buttonBox = new HBox(10, saveButton, deleteButton, deleteUser,logoutButton, exitButton);
        buttonBox.setAlignment(Pos.CENTER);

        VBox vbox = new VBox(grid, buttonBox, statusLabel);
        vbox.setAlignment(Pos.CENTER);

        Scene scene = new Scene(vbox, 500, 300);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void saveOrUpdateSearchCriteria() {
        searchCriteriaService.setLoggedInUserEmail(loggedInUserEmail);
        String brand = brandField.getText();
        String model = modelField.getText();
        String year = yearField.getText();
        String price = priceField.getText();

        boolean actionSuccessful = searchCriteriaService.saveOrUpdateSearchCriteria(brand, model, year, price);
        if (actionSuccessful) {
            checkIfCarsExistForCriteria(brand, model, year, price);
            statusLabel.setText("Search criteria saved/updated successfully.");
            statusLabel.setStyle("-fx-text-fill: black;");
        } else {
            statusLabel.setText("Es existieren bereits Suchkriterien für diese E-Mail-Adresse.\nBitte löschen Sie diese zuerst.");
            statusLabel.setStyle("-fx-text-fill: red;");
        }
    }

    private void deleteSearchCriteria() {
        searchCriteriaService.setLoggedInUserEmail(loggedInUserEmail);
        searchCriteriaService.deleteSearchCriteria();
        statusLabel.setText("Search criteria deleted successfully.");
    }

    private void checkIfCarsExistForCriteria(String brand, String model, String year, String price) {
        new Thread(() -> {
            try {
                WebCrawler webCrawler = new WebCrawler();
                List<Car> cars = webCrawler.crawl(new SearchCriteria(0, loggedInUserEmail, brand, model, year, price));
                Platform.runLater(() -> {
                    if (cars.isEmpty()) {
                        statusLabel.setText("No cars found for these criteria. Modify your search or wait for future updates.");
                        statusLabel.setStyle("-fx-text-fill: red;");
                    } else {
                        statusLabel.setText("Search criteria saved successfully. Cars found: " + cars.size());
                        statusLabel.setStyle("-fx-text-fill: black;");
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                Platform.runLater(() -> statusLabel.setText("Error occurred during the crawl process."));
            }
        }).start();
    }

    private void deleteAccount() {
        int userId = userService.getUserIdByEmail(loggedInUserEmail);
        if (userId != -1) {
            if (confirmDelete()) {
                boolean success = userService.deleteUser(userId);
                Scene currentScene = brandField.getScene();
                if (success) {
                    clearUserData();
                    statusLabel.setText("Account erfolgreich gelöscht.");
                    if (currentScene != null) {
                        Stage stage = (Stage) currentScene.getWindow();
                        if (stage != null) {
                            stage.close();
                        }
                    }
                    redirectToLogin();
                } else {
                    statusLabel.setText("Fehler beim Löschen des Accounts.");
                }
            }
        } else {
            statusLabel.setText("Benutzerkonto nicht gefunden.");
        }
    }
    private void logoutUser(){
        userService.logoutUser();
        Scene currentScene = brandField.getScene();
        if (currentScene != null) {
            Stage stage = (Stage) currentScene.getWindow();
            if (stage != null) {
                stage.close();
            }
        }

        redirectToLogin();
    }


    private boolean confirmDelete() {
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION, "Möchten Sie Ihr Konto wirklich löschen?", ButtonType.YES, ButtonType.NO);
        confirmationAlert.showAndWait();
        return confirmationAlert.getResult() == ButtonType.YES;
    }

    private void clearUserData() {
        loggedInUserEmail = null;
    }

    private void redirectToLogin() {
        LoginRegistrationUI loginUI = new LoginRegistrationUI();
        Stage loginStage = new Stage();
        loginUI.start(loginStage);
    }

    public void setLoggedInUserEmail(String email) {
        this.loggedInUserEmail = email;
    }
}
