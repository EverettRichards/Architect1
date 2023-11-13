package edu.sdccd.cisc191.server.concurrency;

import java.util.concurrent.BlockingQueue;

import edu.sdccd.cisc191.server.ServerStockCandle;

public class FileWriter implements Runnable {
    private BlockingQueue<ServerStockCandle> writerQueue;

    public FileWriter(BlockingQueue<ServerStockCandle> writerQueue) {
        this.writerQueue = writerQueue;
    }

    @Override
    public void run() {
        try {
            while (true) {
                ServerStockCandle serverStockCandle = writerQueue.take();
                try {
                    serverStockCandle.saveAsJsonFile();
                }
                catch (Exception e) {
                    System.out.println("Failed to write. Error: " + e);
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
