package org.example.util;

import javax.mail.*;
import javax.mail.internet.*;
import java.io.IOException;
import java.sql.SQLException;

public class EmailSender {

    private EmailConfiguration emailConfig;

    public EmailSender(EmailConfiguration config) {
        this.emailConfig = config;
    }

    public void sendEmailWithAttachment(String userEmail, String subject, String attachFilePath)
            throws MessagingException, IOException, SQLException {

        Session session = Session.getInstance(emailConfig.getProperties(), emailConfig.getAuthenticator());
        Message msg = new MimeMessage(session);


        msg.setFrom(new InternetAddress(emailConfig.getUserName()));
        InternetAddress[] toAddresses = {new InternetAddress(userEmail)};
        msg.setRecipients(Message.RecipientType.TO, toAddresses);
        msg.setSubject(subject);
        msg.setSentDate(new java.util.Date());

        String messageContent = EmailMessageBuilder.buildEmailMessage();
        MimeBodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setContent(messageContent, "text/html");


        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(messageBodyPart);
        if (attachFilePath != null) {
            MimeBodyPart attachPart = new MimeBodyPart();
            attachPart.attachFile(attachFilePath);
            multipart.addBodyPart(attachPart);
        }

        msg.setContent(multipart);
        Transport.send(msg);
    }
}
