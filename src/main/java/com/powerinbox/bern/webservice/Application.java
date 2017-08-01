package com.powerinbox.bern.webservice;

import com.powerinbox.bern.webservice.config.PostgresConfig;
import com.powerinbox.bern.webservice.config.PostgresProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(PostgresConfig.class)
@EnableConfigurationProperties(PostgresProperties.class)
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}