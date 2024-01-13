module org.example.webcrawlerui {
    requires javafx.controls;
    requires org.jsoup;
    requires java.sql;

    opens org.example.webcrawlerui to javafx.fxml;
    exports org.example.webcrawlerui;
    exports org.example;
    opens org.example to javafx.fxml;
}