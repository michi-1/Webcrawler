package org.example.scheduler;

import java.util.Timer;
import java.util.TimerTask;

public class CrawlingScheduler {
    private Timer timer;

    public CrawlingScheduler() {
        this.timer = new Timer();
    }

    public void startCrawlingTask(Runnable task, long periodInMillis) {
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                task.run();
            }
        };
        timer.scheduleAtFixedRate(timerTask, 0, periodInMillis);
    }
}
