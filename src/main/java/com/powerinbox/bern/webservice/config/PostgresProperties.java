package com.powerinbox.bern.webservice.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

@Component
@ConfigurationProperties(prefix = "pg", locations = "classpath:application.yml")
public class PostgresProperties {
    @NotNull
    private String postgresHost;
    @NotNull
    private String postgresPort;
    @NotNull
    private String username;
    @NotNull
    private String password;
    @NotNull
    private String driverClassName;
    @NotNull
    private String databaseName;

    public String getPostgresHost() {
        return postgresHost;
    }

    public void setPostgresHost(String postgresHost) {
        this.postgresHost = postgresHost;
    }

    public String getPostgresPort() {
        return postgresPort;
    }

    public void setPostgresPort(String postgresPort) {
        this.postgresPort = postgresPort;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDriverClassName() {
        return driverClassName;
    }

    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }
}
