package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DBConnect {
    private static volatile DBConnect instance;
    private Connection connection;
    private static final String USERNAME = "sa";
    private static final String PASSWORD = "123";
    private static final String DB_URL = "jdbc:sqlserver://localhost:1433;databaseName=Hankyo;encrypt=true;trustServerCertificate=true;useUnicode=true&characterEncoding=UTF-8";
    private static final String DRIVER_CLASS = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    private static final Logger LOGGER = Logger.getLogger(DBConnect.class.getName());

    private DBConnect() {
        initializeConnection();
    }

    private void initializeConnection() {
        try {
            Class.forName(DRIVER_CLASS);
            this.connection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
            // Thiết lập auto-commit là true để tránh deadlock
            this.connection.setAutoCommit(true);
            LOGGER.info("Database connection initialized successfully");
        } catch (ClassNotFoundException | SQLException e) {
            LOGGER.log(Level.SEVERE, "Database connection error: " + e.getMessage(), e);
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
                        LOGGER.info("Connection is closed or null, reinitializing...");
                        initializeConnection();
                    }
                }
            }
            // Kiểm tra kết nối còn hoạt động không
            if (!connection.isValid(5)) { // 5 giây timeout
                LOGGER.warning("Connection is not valid, reinitializing...");
                synchronized (this) {
                    initializeConnection();
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Failed to reinitialize database connection", e);
            throw new RuntimeException("Failed to reinitialize database connection", e);
        }
        return connection;
    }

    public void closeConnection() {
        if (connection != null) {
            try {
                if (!connection.isClosed()) {
                    connection.close();
                    LOGGER.info("Database connection closed successfully");
                }
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Error closing database connection", e);
            }
        }
    }

    public void reconnect() {
        synchronized (this) {
            closeConnection();
            initializeConnection();
        }
    }
}
