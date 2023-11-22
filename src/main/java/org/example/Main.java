package org.example;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        String baseUrl = "https://www.autoscout24.at/lst";
        String brand = "bmw";
        String model = "x6";
        String year = "2014";
        String price = "50000";

        try {
            int currentPage = 1;
            boolean hasNextPage = true;

            while (hasNextPage) {
                String searchUrl = buildSearchUrl(baseUrl, model, brand, year, price, currentPage);
                Document document = Jsoup.connect(searchUrl).get();
                String title = document.title();
                System.out.println("Title: " + title);

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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String buildSearchUrl(String baseUrl, String model, String brand, String year, String price, int page) {
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
        stringBuilder.append("page=" + page);  // Add page parameter
        System.out.println(stringBuilder.toString());
        return stringBuilder.toString();
    }
}
