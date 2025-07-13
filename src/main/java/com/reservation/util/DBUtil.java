package com.reservation.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil {
    private static final String DB_URL;

    static {
        try {
            String basePath = System.getProperty("user.dir"); // Project root
            DB_URL = "jdbc:sqlite:" + basePath + "/db/hotel.db";
            System.out.println("✅ Using DB at: " + DB_URL);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to build DB path");
        }
    }

    public static Connection getConnection() throws SQLException, ClassNotFoundException {
        Class.forName("org.sqlite.JDBC");
        return DriverManager.getConnection(DB_URL);
    }
}