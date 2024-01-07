package org.example;

import javafx.application.Platform;
import javafx.scene.control.Label;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WebCrawler {

    private Label statusLabel;
    private CarExtractor carExtractor = new CarExtractor();

    public WebCrawler(Label statusLabel) {
        this.statusLabel = statusLabel;
    }

    public void crawlAndSave(String brand, String model, String year, String price, String filePath) {
        List<Car> cars = new ArrayList<>();
        statusLabel.setText("Status: Crawling...");
        String baseUrl = "https://www.autoscout24.at/lst";
        String searchUrl = "";

        try {
            int currentPage = 1;
            boolean hasNextPage = true;

            while (hasNextPage) {
                searchUrl = buildSearchUrl(baseUrl, model, brand, year, price, currentPage);
                Document document = Jsoup.connect(searchUrl).get();

                Elements productElements = document.select("article.cldt-summary-full-item");
                for (Element productElement : productElements) {
                    Car car = carExtractor.extractCar(productElement);
                    cars.add(car);
                }

                Elements nextPageElements = document.select(".FilteredListPagination_button__41hHM");
                hasNextPage = !nextPageElements.isEmpty();
                currentPage++;
            }

            PDFGenerator pdfGenerator = new PDFGenerator();
            pdfGenerator.createPDF(filePath, cars, searchUrl,brand, model, year, price);
            Platform.runLater(() -> statusLabel.setText("Status: Crawling complete"));
        } catch (IOException e) {
            e.printStackTrace();
            Platform.runLater(() -> statusLabel.setText("Status: Error - " + e.getMessage()));
        }
    }
    private String buildSearchUrl(String baseUrl, String model, String brand, String year, String price, int page) {
        StringBuilder stringBuilder = new StringBuilder(baseUrl);
        if (brand != null) {
            stringBuilder.append("/" + brand);
        }
        if (model != null) {
            stringBuilder.append("/" + model);
        }
        stringBuilder.append("?");
        if (model != year) {
            stringBuilder.append("fregfrom=" + year + "&");
        }
        if (model != year) {
            stringBuilder.append("priceto=" + price + "&");
        }
        stringBuilder.append("page=" + page);
        System.out.println(stringBuilder.toString());
        return stringBuilder.toString();
    }
}
