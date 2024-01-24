package org.example.util;

import java.util.Objects;

public class SearchUrlBuider2 {
    public static String buildSearchUrl2(String baseUrl, String brandId, String modelId, String year, String price, int page) {
        StringBuilder stringBuilder = new StringBuilder(baseUrl);


        if (Objects.equals(year, "") && Objects.equals(price, "")) {
            stringBuilder.append(brandId + "-gebrauchtwagen");
            stringBuilder.append("/" + modelId);
            stringBuilder.append("?page=" + page);

        } else {
            stringBuilder.append("gebrauchtwagenboerse?");
            if (!Objects.equals(brandId, "")) {
                stringBuilder.append("CAR_MODEL/MAKE=" + brandId);
                if (!Objects.equals(modelId, "")) {
                    stringBuilder.append("&CAR_MODEL/MODEL=" + modelId);
                }
            }
            stringBuilder.append("&page=" + page);

            if (!Objects.equals(price, "")) {
                stringBuilder.append("&PRICE_TO=" + price);
            }
            if (!Objects.equals(year, "")) {
                stringBuilder.append("&YEAR_MODEL_FROM=" + year);
            }
        }

        return stringBuilder.toString();
    }
}
