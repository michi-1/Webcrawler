package org.example.core;

import org.example.core.SeleniumWebCrawler;
import org.example.core.WebCrawler;
import org.example.model.Car;
import org.example.model.SearchCriteria;
import org.example.util.CrawlingResultsStorage;
import org.example.util.DataComparator;
import org.example.util.ReportGenerator;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class CrawlingAndComparisonTask implements Runnable {
    private final WebCrawler webCrawlerAutoscout;
    private final SeleniumWebCrawler webCrawlerWillhaben;
    private final ReportGenerator reportGenerator;
    private final CrawlingResultsStorage resultsStorage;
    private final DataComparator dataComparator;
    private final SearchCriteria criteria;

    private final String pdfFilePathAutoscout;
    private final String pdfFilePathWillhaben;
    private final String searchUrlAutoscout;
    private final String searchUrlWillhaben;

    public CrawlingAndComparisonTask(WebCrawler webCrawlerAutoscout, SeleniumWebCrawler webCrawlerWillhaben, ReportGenerator reportGenerator,
                                     CrawlingResultsStorage resultsStorage, DataComparator dataComparator,
                                     SearchCriteria criteria, String pdfFilePathAutoscout, String pdfFilePathWillhaben, String searchUrlAutoscout, String searchUrlWillhaben) {
        this.webCrawlerAutoscout = webCrawlerAutoscout;
        this.webCrawlerWillhaben = webCrawlerWillhaben;
        this.reportGenerator = reportGenerator;
        this.resultsStorage = resultsStorage;
        this.dataComparator = dataComparator;
        this.criteria = criteria;
        this.pdfFilePathAutoscout = pdfFilePathAutoscout;
        this.pdfFilePathWillhaben = pdfFilePathWillhaben;
        this.searchUrlAutoscout = searchUrlAutoscout;
        this.searchUrlWillhaben = searchUrlWillhaben;
    }

    public void run() {
        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        List<Car> currentResultsAutoscout = webCrawlerAutoscout.crawl(criteria);
        List<Car> currentResultsWillhaben = webCrawlerWillhaben.crawl(searchUrlWillhaben);

        List<Car> lastResultsAutoscout = resultsStorage.getLastResults(criteria.getUserEmail() + "_autoscout");
        List<Car> lastResultsWillhaben = resultsStorage.getLastResults(criteria.getUserEmail() + "_willhaben");

        if (lastResultsAutoscout == null || lastResultsWillhaben == null) {
            System.out.println(dateFormat.format(currentDate) + " ---------- " + " Erster Crawling-Durchlauf für " + criteria.getUserEmail() + ". Kein Vergleich möglich.");
            reportGenerator.generateAndSendReport(pdfFilePathAutoscout, criteria, currentResultsAutoscout, searchUrlAutoscout);
            reportGenerator.generateAndSendReport(pdfFilePathWillhaben, criteria, currentResultsWillhaben, searchUrlWillhaben);
        } else {
            if (dataComparator.hasDataChanged(criteria.getUserEmail() + "_autoscout", currentResultsAutoscout, lastResultsAutoscout) ||
                    dataComparator.hasDataChanged(criteria.getUserEmail() + "_willhaben", currentResultsWillhaben, lastResultsWillhaben)) {
                System.out.println(dateFormat.format(currentDate) + " ----------Änderungen entdeckt für " + criteria.getUserEmail() + "----------");
                List<Car> differencesAutoscout = dataComparator.findDifferences(currentResultsAutoscout, lastResultsAutoscout);
                List<Car> differencesWillhaben = dataComparator.findDifferences(currentResultsWillhaben, lastResultsWillhaben);
                System.out.println(dateFormat.format(currentDate) + " Unterschiede für " + criteria.getUserEmail() + " (Autoscout): " + differencesAutoscout);
                System.out.println(dateFormat.format(currentDate) + " Unterschiede für " + criteria.getUserEmail() + " (Willhaben): " + differencesWillhaben);
                reportGenerator.generateAndSendReport(pdfFilePathAutoscout, criteria, currentResultsAutoscout, searchUrlAutoscout);
                reportGenerator.generateAndSendReport(pdfFilePathWillhaben, criteria, currentResultsWillhaben, searchUrlWillhaben);
            } else {
                System.out.println(dateFormat.format(currentDate) + " Keine Änderungen festgestellt für " + criteria.getUserEmail());
            }
        }
        resultsStorage.saveResults(criteria.getUserEmail() + "_autoscout", currentResultsAutoscout);
        resultsStorage.saveResults(criteria.getUserEmail() + "_willhaben", currentResultsWillhaben);
    }
}
