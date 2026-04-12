package config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import exception.database.ApplicationPropertiesException;
import exception.database.ConnectionInitException;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnection {

  private static DBConnection instance;
  private final HikariDataSource dataSource;
  private static final String CONFIG_FILE = "application.properties";

  private DBConnection() {
    Properties props = new Properties();
    try (InputStream input = getClass().getClassLoader().getResourceAsStream(CONFIG_FILE)) {
      if (input == null) {
        throw new ApplicationPropertiesException("Không tìm thấy file application.properties");
      }
      props.load(input);

      HikariConfig config = new HikariConfig();

      config.setJdbcUrl(props.getProperty("mysql.url"));
      config.setUsername(props.getProperty("mysql.username"));
      config.setPassword(props.getProperty("mysql.password"));
      config.setDriverClassName("com.mysql.cj.jdbc.Driver");

      config.setMaximumPoolSize(Integer.parseInt(props.getProperty("hikari.maximum-pool-size", "10")));
      config.setMinimumIdle(Integer.parseInt(props.getProperty("hikari.minimum-idle", "5")));
      config.setIdleTimeout(Long.parseLong(props.getProperty("hikari.idle-timeout", "30000")));
      config.setConnectionTimeout(Long.parseLong(props.getProperty("hikari.connection-timeout", "20000")));

      this.dataSource = new HikariDataSource(config);
    } catch (ApplicationPropertiesException e) {
      throw e;
    } catch (Exception e) {
      throw new ConnectionInitException("Lỗi cấu hình HikariCP: " + e.getMessage(), e);
    }
  }

  public static synchronized DBConnection getInstance() {
    if (instance == null) {
      instance = new DBConnection();
    }
    return instance;
  }

  public Connection getConnection() throws SQLException {
    if (dataSource == null) {
      throw new SQLException("DataSource chưa được khởi tạo.");
    }
    return dataSource.getConnection();
  }
}