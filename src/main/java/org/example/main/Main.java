package org.example.main;

import org.example.core.CrawlingAndComparisonTask;
import org.example.core.SeleniumWebCrawler;
import org.example.core.WebCrawler;
import org.example.model.SearchCriteria;
import org.example.services.SearchCriteriaService;
import org.example.ui.LoginRegistrationUI;
import org.example.util.*;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Main {
    private static final Lock lock = new ReentrantLock();

    public static void main(String[] args) {
        startWebCrawlerTask();
        LoginRegistrationUI.main(args);
    }

    public static void startWebCrawlerTask() {
        SearchCriteriaService searchCriteriaService = new SearchCriteriaService();
        PDFGenerator pdfGenerator = new PDFGenerator();
        EmailConfiguration emailConfig = new EmailConfiguration("carcrawlpro@gmail.com", "idvg yxfh gept zkpa");
        EmailSender emailSender = new EmailSender(emailConfig);
        WebCrawler webCrawler = new WebCrawler();
        DataComparator dataComparator = new DataComparator();
        ReportGenerator reportGenerator = new ReportGenerator(pdfGenerator, emailSender);
        CrawlingResultsStorage resultsStorage = new CrawlingResultsStorage();

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                List<SearchCriteria> allSearchCriteria = searchCriteriaService.getAllSearchCriteria();
                NameToIdConverter brandConverter = new NameToIdConverter("/Users/nahidnourani/Desktop/Semester5/Webcrawler/Mappe1.csv");
                NameToIdConverter modelConverter = new NameToIdConverter("/Users/nahidnourani/Desktop/Semester5/Webcrawler/Mappe2.csv");

                for (SearchCriteria criteria : allSearchCriteria) {
                    new Thread(() -> {
                        lock.lock(); // Das Lock hier anfordern
                        try {
                            SeleniumWebCrawler seleniumWebCrawler = new SeleniumWebCrawler();
                            String brandId = brandConverter.getIdFromName(criteria.getBrand());
                            String modelId = modelConverter.getIdFromName(criteria.getModel());

                            String safeEmail = criteria.getUserEmail().replaceAll("[^a-zA-Z0-9]", "_");
                            String pdfFilePathAutoscout = AppConfig.pdfFilePath + "Autoscout_report_" + safeEmail + ".pdf";
                            String pdfFilePathWillhaben = AppConfig.pdfFilePath + "Willhaben_report_" + safeEmail + ".pdf";

                            String searchUrlAutoscout = SearchUrlBuilder.buildSearchUrl(AppConfig.BASE_URL1, criteria, 1);
                            String searchUrlWillhaben = SearchUrlBuider2.buildSearchUrl2(AppConfig.BASE_URL2, brandId, modelId, criteria.getYear(), criteria.getPrice(), 1);

                            CrawlingAndComparisonTask task = new CrawlingAndComparisonTask(
                                    webCrawler,
                                    seleniumWebCrawler,
                                    reportGenerator,
                                    resultsStorage,
                                    dataComparator,
                                    criteria,
                                    pdfFilePathAutoscout,
                                    pdfFilePathWillhaben,
                                    searchUrlAutoscout,
                                    searchUrlWillhaben
                            );

                            task.run();
                        } finally {
                            lock.unlock();
                        }
                    }).start();
                }
            }
        }, 0, 60000);
    }
}
