package config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import exception.database.ApplicationPropertiesException;
import exception.database.ConnectionInitException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DBConnection implements DatabaseConnectionProvider {

  private static final Logger logger = Logger.getLogger(DBConnection.class.getName());
  private static final String CONFIG_FILE = "application.properties";

  private final HikariDataSource mysqlDataSource;

  public DBConnection() {
    final Properties props = this.loadProperties();

    final HikariConfig mysqlConfig = new HikariConfig();
    mysqlConfig.setJdbcUrl(this.getRequiredProperty(props, "mysql.url"));
    mysqlConfig.setUsername(this.getRequiredProperty(props, "mysql.username"));
    mysqlConfig.setPassword(this.getRequiredProperty(props, "mysql.password"));
    mysqlConfig.setMaximumPoolSize(this.getRequiredInt(props, "hikari.maximum-pool-size"));
    mysqlConfig.setMinimumIdle(this.getRequiredInt(props, "hikari.minimum-idle"));
    mysqlConfig.setIdleTimeout(this.getRequiredLong(props, "hikari.idle-timeout"));
    mysqlConfig.setConnectionTimeout(this.getRequiredLong(props, "hikari.connection-timeout"));

    this.mysqlDataSource = new HikariDataSource(mysqlConfig);
    logger.info("Database Connection Pool initialized successfully!");
  }

  @Override
  public Connection getConnection() throws SQLException {
    logger.info(() -> "getConnection called");
    return mysqlDataSource.getConnection();
  }

  @Override
  public void closePool() {
    logger.info(() -> "closePool called");
    if (mysqlDataSource != null && !mysqlDataSource.isClosed()) {
      mysqlDataSource.close();
      logger.info("Database Connection Pool closed.");
    }
  }

  private Properties loadProperties() {
    final Properties props = new Properties();
    try (InputStream input = DBConnection.class.getClassLoader().getResourceAsStream(CONFIG_FILE)) {
      if (input == null) {
        throw new ApplicationPropertiesException("CONFIG_ERROR",
            "Missing application.properties file");
      }
      props.load(input);
      return props;
    } catch (IOException e) {
      logger.log(Level.SEVERE, "Failed to load application.properties", e);
      throw new ConnectionInitException("CONFIG_ERROR", "Failed to load application.properties", e);
    }
  }

  private String getRequiredProperty(Properties props, String key) {
    final String value = props.getProperty(key);
    if (value == null || value.isBlank()) {
      throw new ApplicationPropertiesException("CONFIG_ERROR", "Missing required property: " + key);
    }
    return value;
  }

  private int getRequiredInt(Properties props, String key) {
    final String value = this.getRequiredProperty(props, key);
    try {
      return Integer.parseInt(value);
    } catch (NumberFormatException e) {
      throw new ApplicationPropertiesException("CONFIG_ERROR",
          "Invalid integer value for property: " + key);
    }
  }

  private long getRequiredLong(Properties props, String key) {
    final String value = this.getRequiredProperty(props, key);
    try {
      return Long.parseLong(value);
    } catch (NumberFormatException e) {
      throw new ApplicationPropertiesException("CONFIG_ERROR",
          "Invalid long value for property: " + key);
    }
  }
}