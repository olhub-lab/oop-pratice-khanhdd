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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DBConnection {

  private static final Logger logger = LoggerFactory.getLogger(DBConnection.class);

  private static DBConnection instance;
  private final HikariDataSource mysqlDataSource;

  private DBConnection() {
    try {
      Properties props = new Properties();
      try (InputStream input = DBConnection.class.getClassLoader()
          .getResourceAsStream("application.properties")) {

        if (input == null) {
          throw new ApplicationPropertiesException("Missing application.properties file"," ");
        }
        props.load(input);
      }

      HikariConfig mysqlConfig = new HikariConfig();

      mysqlConfig.setJdbcUrl(props.getProperty("mysql.url"));
      mysqlConfig.setUsername(props.getProperty("mysql.username"));
      mysqlConfig.setPassword(props.getProperty("mysql.password"));

      mysqlConfig.setMaximumPoolSize(
          Integer.parseInt(props.getProperty("hikari.maximum-pool-size")));
      mysqlConfig.setMinimumIdle(Integer.parseInt(props.getProperty("hikari.minimum-idle")));
      mysqlConfig.setIdleTimeout(Long.parseLong(props.getProperty("hikari.idle-timeout")));
      mysqlConfig.setConnectionTimeout(
          Long.parseLong(props.getProperty("hikari.connection-timeout")));

      this.mysqlDataSource = new HikariDataSource(mysqlConfig);
      logger.info("Database Connection Pool initialized successfully!");

    } catch (IOException | NumberFormatException e) {
      logger.error("Failed to initialize Database Connection Pool", e);
      throw new ConnectionInitException("SQL-500", "Could not connect to MySQL", e);
    }
  }

  public static synchronized DBConnection getInstance() {
    if (instance == null) {
      instance = new DBConnection();
    }
    return instance;
  }

  public Connection getMysqlConnection() throws SQLException {
    return mysqlDataSource.getConnection();
  }

  public void closePool() {
    if (mysqlDataSource != null && !mysqlDataSource.isClosed()) {
      mysqlDataSource.close();
      logger.info("Database Connection Pool closed.");
    }
  }
}