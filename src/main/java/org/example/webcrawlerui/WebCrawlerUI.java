package org.example.webcrawlerui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.EmailSender;
import org.example.WebCrawler;

import java.io.File;

public class WebCrawlerUI extends Application {

    private TextField brandField = new TextField();
    private TextField modelField = new TextField();
    private TextField yearField = new TextField();
    private TextField priceField = new TextField();
    private Label statusLabel = new Label("Status: Ready");

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
        crawlButton.setOnAction(e -> crawlWebsite());

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

        HBox buttonBox = new HBox(crawlButton, exitButton);
        buttonBox.setSpacing(10);
        buttonBox.setAlignment(javafx.geometry.Pos.CENTER);

        VBox vbox = new VBox(grid, buttonBox, statusLabel);

        Scene scene = new Scene(vbox, 400, 250);
        primaryStage.setScene(scene);

        primaryStage.show();
    }

    private void crawlWebsite() {
        // Get input values
        String brand = brandField.getText();
        String model = modelField.getText();
        String year = yearField.getText();
        String price = priceField.getText();
        String userDir = System.getProperty("user.dir");

        // Zielpfad für das 'target'-Verzeichnis bilden
        String targetDirPath = userDir + File.separator + "target";
        String fileName = "autoscout24-report.pdf";
        String filePath = targetDirPath + File.separator + fileName;

        // Instantiate WebCrawler and start crawling
        WebCrawler webCrawler = new WebCrawler(statusLabel);
        webCrawler.crawlAndSave(brand, model, year, price,filePath);
        sendEmailWithPDF(filePath);
    }
    private void sendEmailWithPDF(String filePath) {

        String host = "smtp.gmail.com";
        String port = "587";
        String mailFrom = "carcrawlpro@gmail.com";
        String password = "idvg yxfh gept zkpa";
        String mailTo = "nourani.nahid@gmail.com";
        String subject = "Begrüßung zu Ihrem individuellen Fahrzeugbericht";
        String message = "Sehr geehrter Kunde,<br><br>"
                + "Wir freuen uns, Ihnen unseren neuesten Fahrzeugbericht präsentieren zu können. "
                + "In diesem Bericht finden Sie eine Übersicht über die neuesten Fahrzeuge auf dem Markt, "
                + "die Ihren spezifischen Kriterien entsprechen.<br><br>"
                + "Unsere Experten haben sorgfältig die Daten von verschiedenen Quellen gesammelt und analysiert, "
                + "um Ihnen eine Auswahl an hochwertigen Fahrzeugen vorzustellen. "
                + "Wir verstehen, wie wichtig es ist, das perfekte Auto zu finden, "
                + "das Ihren Bedürfnissen und Wünschen entspricht.<br><br>"
                + "In diesem Bericht werden Sie Informationen zu den neuesten Automodellen, Marken, Preisen und Jahresmodellen finden. "
                + "Wir haben sicherzustellen versucht, dass alle aufgeführten Fahrzeuge Ihren Anforderungen entsprechen und eine Vielzahl von Optionen bieten.<br><br>"
                + "Es war uns eine Freude, diesen Bericht für Sie zu erstellen, "
                + "und wir hoffen, dass er Ihnen bei Ihrer Suche nach einem neuen Fahrzeug hilfreich ist. "
                + "Bei Fragen oder weiterem Bedarf stehen wir Ihnen gerne zur Verfügung.<br><br>"
                + "Vielen Dank, dass Sie unsere Dienstleistungen in Anspruch nehmen. "
                + "Wir wünschen Ihnen viel Erfolg bei der Suche nach Ihrem Traumauto!<br><br>"
                + "Mit freundlichen Grüßen,<br><br>"
                + "Ihr Carcrawlpro Team<br><br>";



        EmailSender mailer = new EmailSender();
        try {
            mailer.sendEmailWithAttachment(host, port, mailFrom, password, mailTo, subject, message, filePath);
            Platform.runLater(() -> statusLabel.setText("E-Mail wurde erfolgreich gesendet."));
        } catch (Exception e) {
            Platform.runLater(() -> statusLabel.setText("Fehler beim Senden der E-Mail: " + e.getMessage()));
            e.printStackTrace();
        }
    }

}
