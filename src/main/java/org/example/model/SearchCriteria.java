package org.example.model;

public class SearchCriteria {
    private int id;
    private String userEmail;
    private String brand;
    private String model;
    private String year;
    private String price;

    public SearchCriteria(int id, String userEmail, String brand, String model, String year, String price) {
        this.id = id;
        this.userEmail = userEmail;
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.price = price;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public boolean isValid() {
        return userEmail != null && !userEmail.isEmpty() &&
                brand != null && !brand.isEmpty() &&
                model != null && !model.isEmpty() &&
                year != null && !year.isEmpty() &&
                price != null && !price.isEmpty();
    }
}

