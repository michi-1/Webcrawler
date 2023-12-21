package org.example;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

import java.io.IOException;
import java.util.List;

public class PDFGenerator {

    public void createPDF(String filePath, List<String> carData) {
        try {
            PdfWriter writer = new PdfWriter(filePath);

            PdfDocument pdf = new PdfDocument(writer);

            Document document = new Document(pdf);

            for (String data : carData) {
                document.add(new Paragraph(data));
            }
            document.close();
            System.out.println("PDF erfolgreich erstellt: " + filePath);
        } catch (IOException e) {
            System.err.println("Ein Fehler ist aufgetreten beim Erstellen des PDFs: " + e.getMessage());
        }
    }
}

