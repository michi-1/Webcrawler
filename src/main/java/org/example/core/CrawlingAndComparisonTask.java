package org.example.core;

import org.example.model.Car;
import org.example.model.SearchCriteria;
import org.example.util.CrawlingResultsStorage;
import org.example.util.DataComparator;
import org.example.util.ReportGenerator;

import java.util.List;

public class CrawlingAndComparisonTask implements Runnable {
    private WebCrawler webCrawler;
    private ReportGenerator reportGenerator;
    private CrawlingResultsStorage resultsStorage;
    private DataComparator dataComparator;
    private SearchCriteria criteria;

    private String pdfFilePath;
    private String searchUrl;

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

    @Override
    public void run() {
        List<Car> currentResults = webCrawler.crawl(criteria);
        List<Car> lastResults = resultsStorage.getLastResults(criteria.getUserEmail());

        System.out.println("Letzte Crawling-Ergebnisse für " + criteria.getUserEmail() + ": " + lastResults);

        System.out.println("Aktuelle Crawling-Ergebnisse für " + criteria.getUserEmail() + ": " + currentResults);

        if (dataComparator.hasDataChanged(criteria.getUserEmail(), currentResults, lastResults)) {
            System.out.println("Änderungen entdeckt für " + criteria.getUserEmail());
            reportGenerator.generateAndSendReport(pdfFilePath, criteria, currentResults, searchUrl);
        } else {
            System.out.println("Keine Änderungen festgestellt für " + criteria.getUserEmail());
        }
        resultsStorage.saveResults(criteria.getUserEmail(), currentResults);
    }
}