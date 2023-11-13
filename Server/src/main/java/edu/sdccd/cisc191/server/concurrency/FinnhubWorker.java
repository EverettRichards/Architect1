package edu.sdccd.cisc191.server.concurrency;

import java.util.concurrent.BlockingQueue;

import edu.sdccd.cisc191.server.ServerStockCandle;
import edu.sdccd.cisc191.common.errors.BadTickerException;

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

                // there are 30 works and the API rate limit is 30 calls per second
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (BadTickerException e) {
            throw new RuntimeException(e);
        }
    }
    
}
