package edu.sdccd.cisc191.server.concurrency;

import java.net.MalformedURLException;
import java.util.concurrent.BlockingQueue;

import com.fasterxml.jackson.core.JsonProcessingException;
import edu.sdccd.cisc191.server.FinnhubNetworking;
import edu.sdccd.cisc191.server.ServerStockCandle;

public class FinnhubWorker implements Runnable {
    private BlockingQueue<FinnhubTask> taskQueue;
    private BlockingQueue<WriterTask> writerQueue;

    public FinnhubWorker(BlockingQueue<FinnhubTask> taskQueue, BlockingQueue<WriterTask> writerQueue) {
        this.taskQueue = taskQueue;
        this.writerQueue = writerQueue;
    }

    @Override
    public void run() {
        try {
            while (true) {
                FinnhubTask task = taskQueue.take();
                String result = ServerStockCandle.fetchCandle(task).toJson();
                WriterTask writerTask = new WriterTask(task.getTicker(), result);

                writerQueue.offer(writerTask);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
    
}
