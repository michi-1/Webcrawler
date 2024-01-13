package org.example.model;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Car {
        private String title;
        private String link;
        private String imageUrl;
        private String price;
        private String transmission;
        private String fuel;
        private String kilometers;

        public Car() {

        }

        public Car(String title, String link, String imageUrl, String price, String transmission, String fuel, String kilometers) {
                this.title = title;
                this.link = link;
                this.imageUrl = imageUrl;
                this.price = price;
                this.transmission = transmission;
                this.fuel = fuel;
                this.kilometers = kilometers;
        }

        public String getTitle() {
                return title;
        }




        public String getLink() {
                return link;
        }




        public String getImageUrl() {
                return imageUrl;
        }




        public String getPrice() {
                return price;
        }



        public String getTransmission() {
                return transmission;
        }


        public String getFuel() {
                return fuel;
        }

        public String getkilometers() {
                return kilometers;
        }


        @Override
        public String toString() {
                return "Car{title=" + getTitle() + ", link=" + getLink() + ", imageURL"
                        + getImageUrl() + ", price" + getPrice() + " ,transmision" +
                        getTransmission() + ", fuel" + getFuel() + ",kilometer" + getkilometers() + "}";
        }

        @Override
        public boolean equals(Object obj) {
                if (this == obj) return true;
                if (obj == null || getClass() != obj.getClass()) return false;
                Car car = (Car) obj;

                List<String> thisCarAttributes = Arrays.asList(getTitle(), getLink(), getImageUrl(), getPrice(), getTransmission(), getFuel(), getkilometers());
                List<String> otherCarAttributes = Arrays.asList(car.getTitle(), car.getLink(), car.getImageUrl(), car.getPrice(), car.getTransmission(), car.getFuel(), car.getkilometers());

                Collections.sort(thisCarAttributes);
                Collections.sort(otherCarAttributes);

                //System.out.println("Car attributes not equal: " + thisCarAttributes + " vs " + otherCarAttributes);
                return thisCarAttributes.equals(otherCarAttributes);
        }


        @Override
        public int hashCode() {
                return Objects.hash(getTitle(), getLink(), getImageUrl(), getPrice(), getTransmission(), getFuel(), getkilometers());
        }
}