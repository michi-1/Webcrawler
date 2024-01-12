package org.example.ui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.model.User;
import org.example.services.UserService;


public class LoginRegistrationUI extends Application {

    private UserService userService;
    private Label statusLabel;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        userService = new UserService();

        primaryStage.setTitle("Login & Registration");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 20, 20, 20));

        Label emailLabel = new Label("Email:");
        Label passwordLabel = new Label("Password:");

        TextField emailTextField = new TextField();
        PasswordField passwordField = new PasswordField();

        Button loginButton = new Button("Login");
        Button registerButton = new Button("Register");

        statusLabel = new Label("");
        statusLabel.setStyle("-fx-text-fill: red;");

        HBox buttonBox = new HBox(10, loginButton, registerButton);
        buttonBox.setSpacing(10);
        buttonBox.setAlignment(Pos.CENTER);

        VBox vbox = new VBox(grid, buttonBox, statusLabel);
        vbox.setAlignment(Pos.CENTER);

        grid.add(emailLabel, 0, 0);
        grid.add(emailTextField, 1, 0);
        grid.add(passwordLabel, 0, 1);
        grid.add(passwordField, 1, 1);
        grid.add(buttonBox, 0, 2, 2, 1);

        loginButton.setOnAction(e -> {
            if (isValidEmail(emailTextField.getText())) {
                handleLogin(emailTextField.getText(), passwordField.getText());
            } else {
                setStatusLabel("Invalid Email. Please enter a valid email address.");
            }
        });

        registerButton.setOnAction(e -> {
            if (isValidEmail(emailTextField.getText())) {
                handleRegister(emailTextField.getText(), passwordField.getText());
            } else {
                setStatusLabel("Invalid Email. Please enter a valid email address.");
            }
        });

        Scene scene = new Scene(vbox, 400, 200);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void handleLogin(String email, String password) {
        User user = userService.loginUser(email, password);
        if (user != null) {
            setStatusLabel("Login successful. Welcome, " + user.getEmail());
            openWebCrawlerUI(email);
        } else {
            setStatusLabel("Login Failed. Invalid email or password.");
        }
    }

    private void openWebCrawlerUI(String email) {
        WebCrawlerUI webCrawlerUI = new WebCrawlerUI();
        webCrawlerUI.setLoggedInUserEmail(email);
        Stage webCrawlerStage = new Stage();
        webCrawlerUI.start(webCrawlerStage);
    }


    private void handleRegister(String email, String password) {
        boolean success = userService.registerUser(email, password);
        if (success) {
            setStatusLabel("Registration Successful!");
        } else {
            setStatusLabel("Registration Failed. Email already exists or an error occurred.");
        }
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(emailRegex);
    }

    private void setStatusLabel(String message) {
        statusLabel.setText(message);
    }
}
