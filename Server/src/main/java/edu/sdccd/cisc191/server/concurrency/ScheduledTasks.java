package edu.sdccd.cisc191.server.concurrency;

import java.time.Instant;
import java.util.concurrent.BlockingQueue;

import edu.sdccd.cisc191.common.entities.Ticker;
import edu.sdccd.cisc191.common.entities.TickerReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTasks {

    @Autowired
    private BlockingQueue<FinnhubTask> taskQueue;

    private String[] tickers;

    TickerReader tickerReader = new TickerReader();

    {
        tickers = tickerReader.getDefaultTickers();
    }

    private Instant lastFetched = Instant.now();

    // every 10 seconds or 10000 miliseconds
    @Scheduled(fixedRate = 60000)
    public void fetchNewCandleData() {
        for(String ticker : tickers) {
            Instant now = Instant.now();
            FinnhubTask task = new FinnhubTask(ticker, "day", lastFetched, now);
            taskQueue.offer(task);
            lastFetched = now;
        }
    }
}
