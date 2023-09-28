package edu.sdccd.cisc191.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class JobProcessorTest {
    public static void main(String[] args) {
        SpringApplication.run(JobProcessorTest.class, args);
    }
}
