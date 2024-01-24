module org.example.webcrawlerui {
    requires javafx.controls;
    requires org.jsoup;
    requires java.mail;
    requires java.sql;
    requires kernel;
    requires layout;
    requires jbcrypt;
    requires selenium.api;
    requires selenium.safari.driver;
    requires selenium.support;

    opens org.example.ui to javafx.fxml;
    exports org.example.ui;

    exports org.example.model;
    exports org.example.services;
    exports org.example.util;


    exports org.example.core;
    exports org.example.doa;
    exports org.example.scheduler;
}
