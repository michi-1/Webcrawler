module org.example.webcrawlerui {
    requires javafx.controls;
    requires org.jsoup;
    requires kernel;
    requires layout;
    requires java.mail;
    requires java.sql;

    opens org.example.webcrawlerui to javafx.fxml;
    exports org.example.webcrawlerui;
}