package edu.sdccd.cisc191.server;

import java.io.File;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

import edu.sdccd.cisc191.server.concurrency.FileWriter;
import edu.sdccd.cisc191.server.concurrency.FinnhubTask;
import edu.sdccd.cisc191.server.concurrency.FinnhubWorker;

import edu.sdccd.cisc191.server.DataMethods;

@SpringBootApplication
@ComponentScan(basePackages = {"edu.sdccd.cisc191"})
@EnableJpaRepositories("edu.sdccd.cisc191.server.repositories")
@EntityScan("edu.sdccd.cisc191.common.entities")
@EnableScheduling
public class Server {

    public static void main(String[] args) {
        new File(DataMethods.stockDirectory).mkdirs();
        new File(DataMethods.stockCandleDirectory).mkdirs();
        DataMethods.instantiateStockIdFile();
        SpringApplication.run(Server.class, args);
    }

    @Bean
    public BlockingQueue<FinnhubTask> taskQueue() {
        int numWorkers = 30;
        BlockingQueue<FinnhubTask> taskQueue = new LinkedBlockingQueue<>();
        BlockingQueue<ServerStockCandle> writerQueue = new LinkedBlockingQueue<>();
        ExecutorService executor = Executors.newFixedThreadPool(numWorkers + 1); // writer

        // this spawns workers and this could should be ran as soon as the program starts
        for (int i = 0; i < numWorkers; i++) {
            executor.execute(new FinnhubWorker(taskQueue, writerQueue));
        }

        executor.execute(new FileWriter(writerQueue));

        return taskQueue;
    }
}
