module org.example.webcrawlerui {
    requires javafx.controls;
    requires org.jsoup;
    requires java.sql;

    opens org.example.webcrawlerui to javafx.fxml;
    exports org.example.webcrawlerui;
}