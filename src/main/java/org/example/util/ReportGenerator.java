package org.example.util;


import org.example.model.SearchCriteria;
import org.example.model.Car;
import javax.mail.MessagingException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class ReportGenerator {
    private PDFGenerator pdfGenerator;
    private EmailSender emailSender;

    public ReportGenerator(PDFGenerator pdfGenerator, EmailSender emailSender) {
        this.pdfGenerator = pdfGenerator;
        this.emailSender = emailSender;
    }

    public void generateAndSendReport(String filePath, SearchCriteria criteria, List<Car> crawledCars,String searchUrl) {
        if (crawledCars == null || crawledCars.isEmpty()) {
            System.err.println("Keine Daten zum Erstellen des PDFs für " + criteria.getUserEmail());
            return;
        }

        try {
            pdfGenerator.createPDF(filePath, crawledCars, searchUrl, criteria.getBrand(), criteria.getModel(), criteria.getYear(), criteria.getPrice());
            System.out.println("PDF erfolgreich erstellt für " + criteria.getUserEmail());

            emailSender.sendEmailWithAttachment(criteria.getUserEmail(), "Ihr Auto-Suchbericht", filePath);
            System.out.println("E-Mail mit Bericht erfolgreich gesendet an " + criteria.getUserEmail());

        } catch (MessagingException | IOException e) {
            System.err.println("Fehler beim Erstellen oder Senden des Berichts für " + criteria.getUserEmail() + ": " + e.getMessage());
            e.printStackTrace();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
