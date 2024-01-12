package org.example.util;

import org.example.model.Car;

import java.util.List;

public class DataComparator {
    public boolean hasDataChanged(String userEmail, List<Car> currentResults, List<Car> lastResults) {
        if(lastResults == null){
            return true;
        }
        return !currentResults.equals(lastResults);
    }

}

