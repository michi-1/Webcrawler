module org.example.webcrawlerui {
    requires javafx.controls;
    requires org.jsoup;
    requires java.mail;
    requires google.api.client;
    requires activation;
    requires kernel;
    requires layout;
    requires io;


    opens org.example.webcrawlerui to javafx.fxml;
    exports org.example.webcrawlerui;
    exports org.example;
    opens org.example to javafx.fxml;
}