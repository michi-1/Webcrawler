package org.example.util;

import org.example.model.SearchCriteria;

public class SearchUrlBuilder {

    public static String buildSearchUrl(String baseUrl, SearchCriteria criteria, int page) {
        StringBuilder stringBuilder = new StringBuilder(baseUrl);
        if (criteria.getBrand() != null) {
            stringBuilder.append("/" + criteria.getBrand());
        }
        if (criteria.getModel() != null) {
            stringBuilder.append("/" + criteria.getModel());
        }
        stringBuilder.append("?");
        if (criteria.getModel() != criteria.getYear()) {
            stringBuilder.append("fregfrom=" + criteria.getYear() + "&");
        }
        if (criteria.getModel() != criteria.getYear()) {
            stringBuilder.append("priceto=" + criteria.getPrice() + "&");
        }
        stringBuilder.append("page=" + page);
        return stringBuilder.toString();
    }
}