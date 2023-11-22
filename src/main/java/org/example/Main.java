package org.example;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class Main {
    public static void main(String[] args) {
        String baseUrl = "https://www.autoscout24.at/lst";
        String brand = "bmw";
        String model = "x6";
        String year = "2014";
        String price = "50000";

        try {
            int currentPage = 1;
            boolean hasNextPage = true;

            while (hasNextPage) {
                String searchUrl = buildSearchUrl(baseUrl, model, brand, year, price, currentPage);
                Document document = Jsoup.connect(searchUrl).get();
                String title = document.title();
                System.out.println("Title: " + title);

                Elements productElements = document.select(".list-page-item");
                for (Element productElement : productElements) {
                    System.out.println("Product: " + productElement.text());
                }

                // Check if there is a next page
                Elements nextPageElements = document.select(".FilteredListPagination_button__41hHM");
                hasNextPage = !nextPageElements.isEmpty();


            }
            sendEmail("nahid_noorani@yahoo.com", "Car Information", emailContent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String buildSearchUrl(String baseUrl, String model, String brand, String year, String price, int page) {
        StringBuilder stringBuilder = new StringBuilder(baseUrl);
        if (brand != null) {
            stringBuilder.append("/" + brand);
        }
        if (model != null) {
            stringBuilder.append("/" + model);
        }
        stringBuilder.append("?");
        if (model != year) {
            stringBuilder.append("fregfrom=" + year + "&");
        }
        if (model != year) {
            stringBuilder.append("priceto=" + price + "&");
        }
        stringBuilder.append("page=" + page);  // Add page parameter
        System.out.println(stringBuilder.toString());
        return stringBuilder.toString();
    }
    private static void sendEmail(String to, String subject, String content) {

        String from = "nourani.nahid@gmail.com";
        String password = "";


        Properties properties = System.getProperties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");


        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, password);
            }
        });

        try {

            MimeMessage message = new MimeMessage(session);

            message.setFrom(new InternetAddress(from));

            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

            message.setSubject(subject);

            message.setText(content);

            Transport.send(message);
            System.out.println("Sent email successfully!");
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }
}
