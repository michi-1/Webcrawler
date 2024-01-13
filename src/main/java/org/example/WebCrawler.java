package org.example;

import javafx.application.Platform;
import javafx.scene.control.Label;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.io.IOException;
import java.util.Objects;

public class WebCrawler {
    private Map<String, String> nameToIdMap = new HashMap<>();
    private Label statusLabel;

    public WebCrawler(Label statusLabel) {
        this.statusLabel = statusLabel;
    }

    public void crawl(String brand, String model, String year, String price) {
        // Clear the status label
        statusLabel.setText("Status: Crawling...");

        String baseUrl = "https://www.autoscout24.at/lst";
        String baseUrl2 ="https://www.willhaben.at/iad/gebrauchtwagen/auto/";
        try {
            int currentPage = 1;
            boolean hasNextPage = true;

            while (hasNextPage) {
                String searchUrl = buildSearchUrl(baseUrl, model, brand, year, price, currentPage);
                String searchUrl2 = buildSearchUrl2(baseUrl2, model, brand, year, price, currentPage);
                Document document = Jsoup.connect(searchUrl).get();
                Document document2 = Jsoup.connect(searchUrl2).get();
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
        if (!Objects.equals(brand, "")) {
            stringBuilder.append("/" + brand);
        }
        if (!Objects.equals(model, "")) {
            stringBuilder.append("/" + model);
        }
        stringBuilder.append("?");
        if (!Objects.equals(year, "")) {
            stringBuilder.append("fregfrom=" + year + "&");
        }
        if (!Objects.equals(price, "")) {
            stringBuilder.append("priceto=" + price + "&");
        }
        stringBuilder.append("page=" + page);
        System.out.println(stringBuilder.toString());
        return stringBuilder.toString();
    }
    private String buildSearchUrl2(String baseUrl, String model, String brand, String year, String price, int page) {
        StringBuilder stringBuilder = new StringBuilder(baseUrl);
        System.out.println(year+price);
        if (Objects.equals(year, "") && Objects.equals(price, "")) {
            stringBuilder.append(brand+"-gebrauchtwagen");
            stringBuilder.append("/" + model );
            stringBuilder.append("?page=" + page);
            System.out.println(stringBuilder.toString());
        }
        else {
            stringBuilder.append("gebrauchtwagenboerse?"  );
            if (!Objects.equals(brand, "")) {
                NameToIdConverter converter = new NameToIdConverter("Mappe1.csv");

                String id = converter.getIdFromName(brand   );
                System.out.println("The ID is: " + id);
                stringBuilder.append("CAR_MODEL/MAKE="+id);
                if(!Objects.equals(model, "")){
                    NameToIdConverter converter2 = new NameToIdConverter("Mappe2.csv");

                    String id2 = converter2.getIdFromName(model);
                    System.out.println("The ID is: " + id2);
                    stringBuilder.append("&CAR_MODEL/MODEL="+id2);

                }


            }
            stringBuilder.append("&page=" + page);

            if (!Objects.equals(price, "")) {
                stringBuilder.append("&PRICE_TO=" + price);
            }
            if (!Objects.equals(year, "")) {
                stringBuilder.append("&YEAR_MODEL_FROM=" + year);
            }

            System.out.println(stringBuilder.toString());

        }


        return stringBuilder.toString();
    }

}
