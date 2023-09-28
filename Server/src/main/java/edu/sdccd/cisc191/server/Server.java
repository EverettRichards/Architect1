package edu.sdccd.cisc191.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories("edu.sdccd.cisc191.server.*")
@ComponentScan(basePackages = { "edu.sdccd.cisc191.server.*" })
@EntityScan("edu.sdccd.cisc191.server.*")
public class Server {

    public static void main(String[] args) {
        SpringApplication.run(Server.class, args);
    }
}
