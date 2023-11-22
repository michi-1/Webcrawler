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
        String url = "https://www.autoscout24.at"; // URL of the website

        try {
            // Create a map for search parameters
            Map<String, String> parameters = new HashMap<>();
            parameters.put("make", "audi");
            parameters.put("model", "a4");


            String searchUrl = buildSearchUrl(url, parameters);


            Document document = Jsoup.connect("https://www.autoscout24.at/lst/mercedes-benz/108").get();


            String title = document.title();
            System.out.println("Title: " + title);


            Elements productElements = document.select(".list-page-item");
            String emailContent = "Title: " + title + "\nProducts:\n";
            for (Element productElement : productElements) {
                emailContent += "Product: " + productElement.text() + "\n";
                System.out.println("Product: " + productElement.text());
            }
            sendEmail("nahid_noorani@yahoo.com", "Car Information", emailContent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static String buildSearchUrl(String baseUrl, Map<String, String> parameters) {
        StringBuilder stringBuilder = new StringBuilder(baseUrl);
        stringBuilder.append("/search");
        if (!parameters.isEmpty()) {
            stringBuilder.append("?");
            for (Map.Entry<String, String> entry : parameters.entrySet()) {
                stringBuilder.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
            }
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        }
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
