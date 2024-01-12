package org.example.core;

import org.example.model.Car;
import org.example.model.SearchCriteria;
import org.example.util.AppConfig;
import org.example.util.SearchUrlBuilder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class WebCrawler {

    private CarExtractor carExtractor = new CarExtractor();
    private ConcurrentHashMap<String, List<Car>> lastCrawledData = new ConcurrentHashMap<>();


    public List<Car> crawl(SearchCriteria criteria ) {
        List<Car> cars = new ArrayList<>();
        int currentPage = 1;
        int totalOffers = 0;

        try {

            String firstPageUrl = SearchUrlBuilder.buildSearchUrl(AppConfig.BASE_URL1, criteria, 1);
            Document firstPageDocument = Jsoup.connect(firstPageUrl).get();
            String offersCountText = firstPageDocument.select("h1.ListHeader_headerTitle__RPFpR span span").first().text();
            totalOffers = Integer.parseInt(offersCountText);


            while (cars.size() < totalOffers) {
                String searchUrl = SearchUrlBuilder.buildSearchUrl(AppConfig.BASE_URL1, criteria, currentPage);
                Document document = Jsoup.connect(searchUrl).get();

                Elements productElements = document.select("article.cldt-summary-full-item");
                for (Element productElement : productElements) {
                    Car car = carExtractor.extractCar(productElement);
                    cars.add(car);
                }
                System.out.println(searchUrl);
                currentPage++;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return cars;
    }

}
