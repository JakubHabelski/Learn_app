package com.learn.app.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class TestDatabaseService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void testConnection() {
        try {
            jdbcTemplate.execute("SELECT 1");
           // System.out.println("Connection successful");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Connection failed");
        }
    }
}