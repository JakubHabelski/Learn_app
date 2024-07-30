package com.learn.app.Config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Configuration
public class DatabaseConfig {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseConfig.class);

    @Value("${spring.datasource.url}")
    private String dbUrl;

    @Value("${spring.datasource.username}")
    private String dbUsername;

    @Value("${spring.datasource.password}")
    private String dbPassword;

    @PostConstruct
    public void testDatabaseConnection() {
        logger.info("Connecting to database: " + dbUrl);
        try (Connection connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword)) {
            logger.info("Successfully connected to the database");
        } catch (SQLException e) {
            logger.error("Failed to connect to the database", e);
        }
    }
}
