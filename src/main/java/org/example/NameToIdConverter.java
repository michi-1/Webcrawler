package org.example;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
public class NameToIdConverter {
    private Map<String, String> nameToIdMap = new HashMap<>();
    public NameToIdConverter(String csvFilePath) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(csvFilePath));
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(";");
                nameToIdMap.put(values[0].trim(), values[1].trim());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


        for (String key : nameToIdMap.keySet()) {
            System.out.println("Key (Name): " + key + ", Value (ID): " + nameToIdMap.get(key));
        }
    }

    public String getIdFromName(String name) {
        return nameToIdMap.get(name);
    }
}
