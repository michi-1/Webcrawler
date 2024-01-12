package org.example.main;


import org.example.core.CrawlingAndComparisonTask;
import org.example.core.WebCrawler;
import org.example.model.SearchCriteria;
import org.example.services.SearchCriteriaService;
import org.example.ui.LoginRegistrationUI;
import org.example.util.*;


import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Main {
    public static void main(String[] args) {

        new Thread(Main::startWebCrawlerTask).start();

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
                for (SearchCriteria criteria : allSearchCriteria) {
                    String pdfFilePath = AppConfig.pdfFilePath + "report_" + criteria.getUserEmail().replaceAll("[^a-zA-Z0-9]", "_") + ".pdf";
                    String searchUrl = SearchUrlBuilder.buildSearchUrl(AppConfig.BASE_URL1, criteria, 1);


                    CrawlingAndComparisonTask task = new CrawlingAndComparisonTask(webCrawler, reportGenerator,
                            resultsStorage, dataComparator, criteria, pdfFilePath, searchUrl);
                    new Thread(task).start();
                }
            }
        }, 0, 60000);
    }
}