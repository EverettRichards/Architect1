package edu.sdccd.cisc191.server.concurrency;

import java.util.concurrent.BlockingQueue;

public class FileWriter implements Runnable {
    private BlockingQueue<WriterTask> writerQueue;

    public FileWriter(BlockingQueue<WriterTask> writerQueue) {
        this.writerQueue = writerQueue;
    }

    @Override
    public void run() {
        try {
            while (true) {
                WriterTask task = writerQueue.take();
                something.saveCandleData(task);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
