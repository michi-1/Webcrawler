module org.example.webcrawlerui {
    requires javafx.controls;
    requires org.jsoup;

    opens org.example.webcrawlerui to javafx.fxml;
    exports org.example.webcrawlerui;
}