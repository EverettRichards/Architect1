package edu.sdccd.cisc191.server.concurrency;

import java.net.MalformedURLException;
import java.util.concurrent.BlockingQueue;

import com.fasterxml.jackson.core.JsonProcessingException;
import edu.sdccd.cisc191.server.FinnhubNetworking;
import edu.sdccd.cisc191.server.ServerStockCandle;

public class FinnhubWorker implements Runnable {
    private BlockingQueue<FinnhubTask> taskQueue;
    private BlockingQueue<ServerStockCandle> writerQueue;

    public FinnhubWorker(BlockingQueue<FinnhubTask> taskQueue, BlockingQueue<ServerStockCandle> writerQueue) {
        this.taskQueue = taskQueue;
        this.writerQueue = writerQueue;
    }

    @Override
    public void run() {
        try {
            while (true) {
                FinnhubTask task = taskQueue.take();
                ServerStockCandle candleData = ServerStockCandle.fetchCandle(task);

                writerQueue.offer(candleData);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
}
