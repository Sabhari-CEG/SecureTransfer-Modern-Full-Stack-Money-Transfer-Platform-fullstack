package com.sabhari.app.transaction.Database;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Component
public class DatabaseConnection {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseConnection.class);
    
    private String dbUrl="jdbc:mysql://localhost:3306/transactions";
    
    private String dbUsername="root";
    
    private String dbPassword="";
    
    private Connection connection;

    public Connection initConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
            logger.info("Database connection established successfully");
            System.err.println("Connected to db Succesfully!");
            return connection;
        } catch (ClassNotFoundException e) {
            logger.error("MySQL JDBC Driver not found: {}", e.getMessage());
            throw new RuntimeException("Database driver not found", e);
        } catch (SQLException e) {
            logger.error("Connection failed! Error: {}", e.getMessage());
            throw new RuntimeException("Failed to connect to database", e);
        }
    }

    public boolean isConnectionActive() {
        try {
            if (connection != null && !connection.isClosed()) {
                return connection.isValid(5);
            }
            return false;
        } catch (SQLException e) {
            logger.error("Error checking connection: {}", e.getMessage());
            return false;
        }
    }

    public Connection getConnection() {
        try {
            if (connection == null || !isConnectionActive()) {
                return initConnection();
            }
            return connection;
        } catch (Exception e) {
            logger.error("Error getting connection: {}", e.getMessage());
            throw new RuntimeException("Failed to get database connection", e);
        }
    }

    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                logger.info("Database connection closed successfully");
            }
        } catch (SQLException e) {
            logger.error("Error closing connection: {}", e.getMessage());
        }
    }
}