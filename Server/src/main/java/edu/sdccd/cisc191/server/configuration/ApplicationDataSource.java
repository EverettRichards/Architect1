package edu.sdccd.cisc191.server.configuration;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

// @Configuration
// @PropertySource("classpath:application.yml")
public class ApplicationDataSource {
    
    @Autowired
    private Environment environment;

    @Bean
    public DataSource dataSource() {
        DataSourceBuilder dsBuilder = DataSourceBuilder.create();
        
        dsBuilder.driverClassName(environment.getRequiredProperty("spring.datasource.driverClassName"));
        dsBuilder.url(environment.getRequiredProperty("spring.datasource.url"));
        dsBuilder.username(environment.getRequiredProperty("spring.datasource.username"));
        dsBuilder.password(environment.getRequiredProperty("spring.datasource.password"));

        return  dsBuilder.build();
    }
}
