package org.example.util;

import org.example.model.Car;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CrawlingResultsStorage {
    private Map<String, List<Car>> lastCrawledData = new HashMap<>();

    public void saveResults(String userEmail, List<Car> results) {
        lastCrawledData.put(userEmail, results);
    }

    public List<Car> getLastResults(String userEmail) {
        return lastCrawledData.getOrDefault(userEmail, null);
    }
}

