package com.expensetracker.util;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Database Utility class managing MySQL JDBC connections and HikariCP connection pooling.
 * 
 * Provides:
 * 1. High-performance connection pooling via HikariCP.
 * 2. Automated schema initialization on first startup.
 * 3. Graceful fallback to standard DriverManager if pool initialization encounters issues.
 * 4. Safe resource closing helpers.
 */
public class DBUtil {
    private static final Logger LOGGER = Logger.getLogger(DBUtil.class.getName());

    private static final String DEFAULT_URL = "jdbc:mysql://localhost:3306/expense_tracker?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC&createDatabaseIfNotExist=true&characterEncoding=UTF-8";
    private static final String DEFAULT_USER = "root";
    private static final String DEFAULT_PASSWORD = "";
    private static final String DEFAULT_DRIVER = "com.mysql.cj.jdbc.Driver";

    private static HikariDataSource dataSource;
    private static boolean schemaInitialized = false;

    static {
        initializeDataSource();
    }

    private DBUtil() {
        // Private constructor for utility class
    }

    /**
     * Initializes the HikariCP DataSource using db.properties or environment overrides.
     */
    private static synchronized void initializeDataSource() {
        if (dataSource != null && !dataSource.isClosed()) {
            return;
        }

        Properties props = new Properties();
        try (InputStream is = DBUtil.class.getResourceAsStream("/db.properties")) {
            if (is != null) {
                props.load(is);
                LOGGER.info("Loaded database configuration from /db.properties");
            } else {
                LOGGER.info("db.properties not found in classpath. Using default local MySQL settings.");
            }
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Failed to load db.properties: " + e.getMessage(), e);
        }

        // Support environment variable and system property overrides
        String url = getSetting("MYSQL_URL", "db.url", props.getProperty("db.url", DEFAULT_URL));
        // Render provides mysql:// URLs but JDBC needs jdbc:mysql://
        if (url != null && url.startsWith("mysql://")) {
            url = "jdbc:" + url;
        }
        String user = getSetting("MYSQL_USER", "db.user", props.getProperty("db.user", DEFAULT_USER));
        String password = getSetting("MYSQL_PASSWORD", "db.password", props.getProperty("db.password", DEFAULT_PASSWORD));
        String driver = props.getProperty("db.driver", DEFAULT_DRIVER);

        try {
            Class.forName(driver);

            HikariConfig config = new HikariConfig();
            config.setDriverClassName(driver);
            config.setJdbcUrl(url);
            config.setUsername(user);
            config.setPassword(password);

            // Pool tuning
            config.setMaximumPoolSize(Integer.parseInt(props.getProperty("hikari.maximumPoolSize", "10")));
            config.setMinimumIdle(Integer.parseInt(props.getProperty("hikari.minimumIdle", "2")));
            config.setIdleTimeout(Long.parseLong(props.getProperty("hikari.idleTimeout", "30000")));
            config.setConnectionTimeout(Long.parseLong(props.getProperty("hikari.connectionTimeout", "10000")));
            config.setMaxLifetime(Long.parseLong(props.getProperty("hikari.maxLifetime", "1800000")));
            config.setPoolName("ExpenseTrackerPool");

            dataSource = new HikariDataSource(config);
            LOGGER.info("HikariCP connection pool initialized successfully for " + url);

            // Initialize database tables if needed
            initializeSchema();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to initialize HikariCP connection pool: " + e.getMessage(), e);
        }
    }

    private static String getSetting(String envName, String propName, String defaultValue) {
        String envVal = System.getenv(envName);
        if (envVal != null && !envVal.trim().isEmpty()) {
            return envVal;
        }
        String sysVal = System.getProperty(propName);
        if (sysVal != null && !sysVal.trim().isEmpty()) {
            return sysVal;
        }
        return defaultValue;
    }

    /**
     * Obtains an active connection from the pool, or fallback to DriverManager.
     * @return java.sql.Connection
     * @throws SQLException if a database access error occurs
     */
    public static Connection getConnection() throws SQLException {
        if (dataSource != null && !dataSource.isClosed()) {
            return dataSource.getConnection();
        }

        // Fallback directly to DriverManager
        LOGGER.warning("HikariDataSource is not ready. Attempting direct DriverManager connection...");
        return DriverManager.getConnection(DEFAULT_URL, DEFAULT_USER, DEFAULT_PASSWORD);
    }

    /**
     * Automatically creates necessary tables if they do not already exist.
     */
    public static synchronized void initializeSchema() {
        if (schemaInitialized) {
            return;
        }

        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {
            // 1. users table
            stmt.execute(
                "CREATE TABLE IF NOT EXISTS users (" +
                "  user_id VARCHAR(36) PRIMARY KEY, " +
                "  name VARCHAR(100) NOT NULL, " +
                "  email VARCHAR(150) NOT NULL UNIQUE, " +
                "  password_hash VARCHAR(255) NOT NULL, " +
                "  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;"
            );

            // 2. expenses table
            stmt.execute(
                "CREATE TABLE IF NOT EXISTS expenses (" +
                "  id VARCHAR(36) PRIMARY KEY, " +
                "  user_id VARCHAR(36) NOT NULL, " +
                "  amount DECIMAL(10, 2) NOT NULL, " +
                "  description VARCHAR(255) NOT NULL, " +
                "  category VARCHAR(50) NOT NULL, " +
                "  expense_date DATE NOT NULL, " +
                "  notes TEXT, " +
                "  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                "  INDEX idx_user_date (user_id, expense_date)" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;"
            );

            // 3. budgets table
            stmt.execute(
                "CREATE TABLE IF NOT EXISTS budgets (" +
                "  id VARCHAR(50) PRIMARY KEY, " +
                "  user_id VARCHAR(36) NOT NULL, " +
                "  month INT NOT NULL, " +
                "  year INT NOT NULL, " +
                "  amount DECIMAL(10, 2) NOT NULL, " +
                "  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, " +
                "  UNIQUE KEY uq_user_month_year (user_id, year, month)" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;"
            );

            schemaInitialized = true;
            LOGGER.info("MySQL database schema verified/initialized successfully.");
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Automatic schema initialization check warning: " + e.getMessage());
        }
    }

    /**
     * Gracefully closes any open AutoCloseable resources (Connection, Statement, ResultSet).
     */
    public static void close(AutoCloseable... resources) {
        if (resources == null) return;
        for (AutoCloseable res : resources) {
            if (res != null) {
                try {
                    res.close();
                } catch (Exception e) {
                    LOGGER.log(Level.FINE, "Error closing resource: " + e.getMessage(), e);
                }
            }
        }
    }

    /**
     * Shuts down the connection pool upon application shutdown.
     */
    public static synchronized void shutdown() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
            LOGGER.info("HikariCP connection pool closed.");
        }
    }
}
