package uz.app.service.repository;

import java.sql.Connection;
import java.sql.DriverManager;

public class DbConnection {
    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5432/bot",
                    "postgres",
                    "1483"
            );
        } catch (Exception e) {
            throw new RuntimeException("Error connecting to DB", e);
        }
    }
}

