package org.example.util;

import org.example.model.Car;

import java.util.ArrayList;
import java.util.List;

public class DataComparator {
    public boolean hasDataChanged(String userEmail, List<Car> currentResults, List<Car> lastResults) {
        if(lastResults == null){
            return true;
        }
        return !currentResults.equals(lastResults);
    }
    public List<Car> findDifferences(List<Car> currentResults, List<Car> lastResults) {
        List<Car> differences = new ArrayList<>();
        if (lastResults == null || currentResults == null) {
            return differences;
        }

        for (Car currentCar : currentResults) {
            if (!lastResults.contains(currentCar)) {
                differences.add(currentCar);
            }
        }

        for (Car lastCar : lastResults) {
            if (!currentResults.contains(lastCar)) {
                differences.add(lastCar);
            }
        }

        return differences;
    }

}

