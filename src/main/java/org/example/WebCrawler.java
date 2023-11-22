package org.example;

import javafx.application.Platform;
import javafx.scene.control.Label;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class WebCrawler {

    private Label statusLabel;

    public WebCrawler(Label statusLabel) {
        this.statusLabel = statusLabel;
    }

    public void crawl(String brand, String model, String year, String price) {
        // Clear the status label
        statusLabel.setText("Status: Crawling...");

        String baseUrl = "https://www.autoscout24.at/lst";

        try {
            int currentPage = 1;
            boolean hasNextPage = true;

            while (hasNextPage) {
                String searchUrl = buildSearchUrl(baseUrl, model, brand, year, price, currentPage);
                Document document = Jsoup.connect(searchUrl).get();

                // Process the document, e.g., print the title
                String title = document.title();
                System.out.println("Title: " + title);

                // Process product elements
                Elements productElements = document.select(".list-page-item");
                for (Element productElement : productElements) {
                    System.out.println("Product: " + productElement.text());
                }

                // Check if there is a next page
                Elements nextPageElements = document.select(".FilteredListPagination_button__41hHM");
                hasNextPage = !nextPageElements.isEmpty();

                // Move to the next page
                currentPage++;
            }

            // Update the status label
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
