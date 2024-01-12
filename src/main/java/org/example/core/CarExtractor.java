package org.example.core;

import org.example.model.Car;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class CarExtractor {

    public Car extractCar(Element productElement) {

        String anzahl = extractText(productElement.select("h1"));

        String title = extractText(productElement.select("h2"));
        String link = "https://www.autoscout24.at" + productElement.select("a[href]").attr("href");
        String imageUrl = extractImageSrc(productElement.select("picture.NewGallery_picture__fNsZr img"));
        String price = productElement.select("p.Price_price__APlgs").text();
        Elements vehicleDetails = productElement.select("div.VehicleDetailTable_container__XhfV1 span.VehicleDetailTable_item__4n35N");
        String kilometers = "";
        String transmission = "";
        String fuel = "";

        for (Element detail : vehicleDetails) {
            String text = detail.text();
            if (text.contains("km")) {
                kilometers = text;
            } else if (text.equalsIgnoreCase("Automatik") || text.equalsIgnoreCase("Schaltgetriebe")) {
                transmission = text;
            } else if (text.equalsIgnoreCase("Benzin") || text.equalsIgnoreCase("Diesel") || text.equalsIgnoreCase("Elektro/Benzin" ) || text.equalsIgnoreCase("Elektro") || text.equalsIgnoreCase("Elektro/Diesel") || text.equalsIgnoreCase("Wasserstoff") || text.equalsIgnoreCase("Autogas (LPG)") || text.equalsIgnoreCase("Erdgas (CNG)") || text.equalsIgnoreCase("Sonstiges")) {
                fuel = text;
            }
        }
        return new Car(title, link, imageUrl, price, transmission, fuel, kilometers);
    }
    private String extractText(Elements elements) {
        return elements.isEmpty() ? "Nicht verfügbar" : elements.text();
    }

    private String extractImageSrc(Elements elements) {
        return elements.isEmpty() ? "Bild nicht verfügbar" : elements.attr("src");
    }
}

