package org.example.util;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Link;
import com.itextpdf.kernel.pdf.action.PdfAction;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.layout.property.TextAlignment;
import org.example.model.Car;
import java.io.IOException;
import java.util.List;

public class PDFGenerator {

    public void createPDF(String filePath, List<Car> carData, String searchUrl, String brand, String model, String year, String price) {
        try {
            PdfWriter writer = new PdfWriter(filePath);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);
            String linkText = String.format("Auto-Liste basierend auf Ihrer Suche bei AutoScout24\n%s %s aus dem Jahr %s bis zu einem Preis von %s\n\n", brand, model, year, price);
            Paragraph searchUrlParagraph = new Paragraph();
            Link urlLink = new Link(linkText,PdfAction.createURI(searchUrl));
            urlLink.setBold();
            urlLink.setUnderline();
            urlLink.setFontColor(ColorConstants.BLUE);
            searchUrlParagraph.add(urlLink);
            searchUrlParagraph.setTextAlignment(TextAlignment.CENTER);
            document.add(searchUrlParagraph);

            for (Car car : carData) {

                Paragraph paragraph = new Paragraph();
                Link titleLink = new Link(car.getTitle(), PdfAction.createURI(car.getLink()));
                titleLink.setFontColor(ColorConstants.BLUE);
                titleLink.setUnderline();
                paragraph.add(titleLink);


                paragraph.add("\nPrice: " + car.getPrice())
                        .add("\nKilometers: " + car.getkilometers())
                        .add("\nTransmission: " + car.getTransmission())
                        .add("\nFuel Type: " + car.getFuel());

                document.add(paragraph);
            }

            document.close();
            System.out.println("PDF erfolgreich erstellt: " + filePath);
        } catch (IOException e) {
            System.err.println("Ein Fehler ist aufgetreten beim Erstellen des PDFs: " + e.getMessage());
        }
    }
}
