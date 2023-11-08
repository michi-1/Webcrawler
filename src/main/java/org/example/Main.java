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
        String url = "https://www.autoscout24.at"; // URL of the website

        try {
            // Create a map for search parameters
            Map<String, String> parameters = new HashMap<>();
            parameters.put("make", "audi");
            parameters.put("model", "a4");


            String searchUrl = buildSearchUrl(url, parameters);


            Document document = Jsoup.connect("https://www.autoscout24.at/lst/mercedes-benz").get();


            String title = document.title();
            System.out.println("Title: " + title);


            Elements productElements = document.select(".list-page-item");
            for (Element productElement : productElements) {

                System.out.println("Product: " + productElement.text());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static String buildSearchUrl(String baseUrl, Map<String, String> parameters) {
        StringBuilder stringBuilder = new StringBuilder(baseUrl);
        stringBuilder.append("/search");
        if (!parameters.isEmpty()) {
            stringBuilder.append("?");
            for (Map.Entry<String, String> entry : parameters.entrySet()) {
                stringBuilder.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
            }
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        }
        return stringBuilder.toString();
    }
}
