package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnect {
    private static volatile DBConnect instance;
    private Connection connection;
    private static final String USERNAME = "sa";
    private static final String PASSWORD = "123";
    private static final String DB_URL = "jdbc:sqlserver://BEARXAM\\MSSQLSERVER01:1433;databaseName=Hankyo;encrypt=true;trustServerCertificate=true";
    private static final String DRIVER_CLASS = "com.microsoft.sqlserver.jdbc.SQLServerDriver";

    private DBConnect() {
        try {
            Class.forName(DRIVER_CLASS);
            this.connection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException("Database connection error: " + e.getMessage(), e);
        }
    }

    public static DBConnect getInstance() {
        if (instance == null) {
            synchronized (DBConnect.class) {
                if (instance == null) {
                    instance = new DBConnect();
                }
            }
        }
        return instance;
    }

    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                synchronized (this) {
                    if (connection == null || connection.isClosed()) {
                        connection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to reinitialize database connection", e);
        }
        return connection;
    }
}
