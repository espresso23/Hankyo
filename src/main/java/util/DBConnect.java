package util;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DBConnect {
    private static volatile DBConnect instance;
    private static HikariDataSource dataSource;
    private static final String USERNAME = "sa";
    private static final String PASSWORD = "123";
    private static final String DB_URL = "jdbc:sqlserver://localhost:1433;databaseName=Hankyo;encrypt=true;trustServerCertificate=true";
    private static final String DRIVER_CLASS = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    private static final Logger LOGGER = Logger.getLogger(DBConnect.class.getName());
    
    // Cấu hình connection pool
    private static final int MAX_POOL_SIZE = 20;
    private static final int MIN_IDLE = 10;
    private static final int IDLE_TIMEOUT = 600000; // 10 phút
    private static final int CONNECTION_TIMEOUT = 60000; // 1 phút
    private static final int MAX_LIFETIME = 3600000; // 1 giờ

    protected DBConnect() {
        initializeConnectionPool();
    }

    private void initializeConnectionPool() {
        try {
            HikariConfig config = new HikariConfig();
            config.setDriverClassName(DRIVER_CLASS);
            config.setJdbcUrl(DB_URL);
            config.setUsername(USERNAME);
            config.setPassword(PASSWORD);
            
            // Cấu hình connection pool
            config.setMaximumPoolSize(MAX_POOL_SIZE);
            config.setMinimumIdle(MIN_IDLE);
            config.setIdleTimeout(IDLE_TIMEOUT);
            config.setConnectionTimeout(CONNECTION_TIMEOUT);
            config.setMaxLifetime(MAX_LIFETIME);
            
            // Cấu hình thêm
            config.addDataSourceProperty("cachePrepStmts", "true");
            config.addDataSourceProperty("prepStmtCacheSize", "250");
            config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
            config.addDataSourceProperty("useServerPrepStmts", "true");
            
            // Tạo connection pool
            dataSource = new HikariDataSource(config);
            LOGGER.info("Database connection pool initialized successfully");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Database connection pool initialization error: " + e.getMessage(), e);
            throw new RuntimeException("Database connection pool initialization error", e);
        }
    }

    public static synchronized DBConnect getInstance() {
        if (instance == null) {
            instance = new DBConnect();
        }
        return instance;
    }

    public Connection getConnection() {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Failed to get database connection", e);
            throw new RuntimeException("Failed to get database connection", e);
        }
    }

    public void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Error closing database connection", e);
            }
        }
    }

    public void shutdown() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
            LOGGER.info("Database connection pool closed successfully");
        }
    }
}