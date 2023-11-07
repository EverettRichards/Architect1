package edu.sdccd.cisc191.server.concurrency;

import java.util.concurrent.BlockingQueue;

import edu.sdccd.cisc191.server.FinnhubNetworking;

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
                String result = something.fetchData(task);
                WriterTask writerTask = new WriterTask(task.getTicker(), result);

                writerQueue.offer(writerTask);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
}
