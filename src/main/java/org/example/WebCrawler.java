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


    public WebCrawler(Label statusLabel) {
        this.statusLabel = statusLabel;

    }
    public void crawlAndSave(String brand, String model, String year, String price, String filePath) {
        List<String> carData = new ArrayList<>();
        statusLabel.setText("Status: Crawling...");
        String baseUrl = "https://www.autoscout24.at/lst";

        try {
            int currentPage = 1;
            boolean hasNextPage = true;

            while (hasNextPage) {
                String searchUrl = buildSearchUrl(baseUrl, model, brand, year, price, currentPage);
                Document document = Jsoup.connect(searchUrl).get();
                int productNumber = 1;
                String title = document.title();
                System.out.println("Title: " + title);

                Elements productElements = document.select(".list-page-item");
                for (Element productElement : productElements) {
                    String productInfo = productElement.text();
                    productInfo = productInfo.replaceAll("ZurückWeiter \\d+ / \\d+", "").trim();
                    String numberedProductInfo = productNumber + ". " + productInfo;
                    System.out.println(numberedProductInfo);
                    carData.add(numberedProductInfo);
                    productNumber++;
                }

                Elements nextPageElements = document.select(".FilteredListPagination_button__41hHM");
                hasNextPage = !nextPageElements.isEmpty();

                currentPage++;
            }


            PDFGenerator pdfGenerator = new PDFGenerator();
            pdfGenerator.createPDF(filePath, carData);
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
