package org.example.util;

import java.util.Properties;
import javax.mail.*;

public class EmailConfiguration {
    private static final String HOST = "smtp.gmail.com";
    private static final String PORT = "587";

    private String userName ;
    private String password ;

    public EmailConfiguration(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public Properties getProperties() {
        Properties properties = new Properties();
        properties.put("mail.smtp.host", HOST);
        properties.put("mail.smtp.port", PORT);
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        return properties;
    }

    public Authenticator getAuthenticator() {
        return new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(userName, password);
            }
        };
    }

    public String getUserName() {
        return userName;
    }
}
