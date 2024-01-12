package org.example.model;

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

        public void setTitle(String title) {
                this.title = title;
        }

        // Getter und Setter für link
        public String getLink() {
                return link;
        }

        public void setLink(String link) {
                this.link = link;
        }

        // Getter und Setter für imageUrl
        public String getImageUrl() {
                return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
                this.imageUrl = imageUrl;
        }

        // Getter und Setter für price
        public String getPrice() {
                return price;
        }

        public void setPrice(String price) {
                this.price = price;
        }

        // Getter und Setter für transmission
        public String getTransmission() {
                return transmission;
        }

        public void setTransmission(String transmission) {
                this.transmission = transmission;
        }

        // Getter und Setter für fuel
        public String getFuel() {
                return fuel;
        }

        public void setFuel(String fuel) {
                this.fuel = fuel;
        }
        public String getkilometers() {
                return kilometers;
        }

        public void setkilometers(String kilometers) {
                this.kilometers = kilometers;
        }

        @Override
        public String toString(){
                return "Car{title=" + getTitle() + ", link=" + getLink() + ", imageURL"
                        + getImageUrl() + ", price" + getPrice() + " ,transmision" +
                        getTransmission() + ", fuel"  + getFuel() + ",kilometer" + getkilometers() + "}";
        }
}
