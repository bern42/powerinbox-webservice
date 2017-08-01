package com.powerinbox.bern.webservice.config;

import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
@ComponentScan("com.powerinbox.bern.webservice")
public class PostgresConfig {

    @Bean
    public PostgresProperties postgresProperties() {
        return new PostgresProperties();
    }

    @Bean(name = "postgresDataSource")
    public DataSource dataSource() {
        String connectionUrl = "jdbc:postgresql://" + postgresProperties().getPostgresHost() + ":" + postgresProperties().getPostgresPort() + "/" + postgresProperties().getDatabaseName();
        return DataSourceBuilder.create()
                .username(postgresProperties().getUsername())
                .password(postgresProperties().getPassword())
                .driverClassName(postgresProperties().getDriverClassName())
                .url(connectionUrl).build();
    }


}
