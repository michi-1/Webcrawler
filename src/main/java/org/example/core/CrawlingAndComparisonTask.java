package org.example.core;

import org.example.model.Car;
import org.example.model.SearchCriteria;
import org.example.util.CrawlingResultsStorage;
import org.example.util.DataComparator;
import org.example.util.ReportGenerator;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class CrawlingAndComparisonTask implements Runnable {
    private final WebCrawler webCrawler;
    private final ReportGenerator reportGenerator;
    private final CrawlingResultsStorage resultsStorage;
    private final DataComparator dataComparator;
    private final SearchCriteria criteria;

    private final String pdfFilePath;
    private final String searchUrl;

    public CrawlingAndComparisonTask(WebCrawler webCrawler, ReportGenerator reportGenerator,
                                     CrawlingResultsStorage resultsStorage, DataComparator dataComparator,
                                     SearchCriteria criteria, String pdfFilePath, String searchUrl) {
        this.webCrawler = webCrawler;
        this.reportGenerator = reportGenerator;
        this.resultsStorage = resultsStorage;
        this.dataComparator = dataComparator;
        this.criteria = criteria;
        this.pdfFilePath = pdfFilePath;
        this.searchUrl = searchUrl;
    }

    public void run() {
        Date currentDate = new Date(); // Aktuelles Datum und Uhrzeit
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // Format für Datum und Uhrzeit

        List<Car> currentResults = webCrawler.crawl(criteria);
        List<Car> lastResults = resultsStorage.getLastResults(criteria.getUserEmail());

        if (lastResults == null) {
            System.out.println(dateFormat.format(currentDate) + " ---------- " + " Erster Crawling-Durchlauf für " + criteria.getUserEmail() + ". Kein Vergleich möglich.");
            reportGenerator.generateAndSendReport(pdfFilePath, criteria, currentResults, searchUrl);
        } else {
            if (dataComparator.hasDataChanged(criteria.getUserEmail(), currentResults, lastResults)) {
                System.out.println(dateFormat.format(currentDate) + " ----------Änderungen entdeckt für " + criteria.getUserEmail() + "----------");
                List<Car> differences = dataComparator.findDifferences(currentResults, lastResults);
                System.out.println(dateFormat.format(currentDate) + " Unterschiede für " + criteria.getUserEmail() + ": " + differences);
                reportGenerator.generateAndSendReport(pdfFilePath, criteria, currentResults, searchUrl);
            } else {
                System.out.println(dateFormat.format(currentDate) + " Keine Änderungen festgestellt für " + criteria.getUserEmail());
            }
        }
        resultsStorage.saveResults(criteria.getUserEmail(), currentResults);
    }
}