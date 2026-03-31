import config.DBConnection;
import repository.OrderRepository;
import repository.jdbc.MySqlOrderRepository;
import repository.memory.OrderRepositoryImpl;
import service.order.OrderService;
import service.order.impl.OrderServiceImpl;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Logger;

public class Main {

  private static final Logger logger = Logger.getLogger(Main.class.getName());
  private static final String STORAGE_MODE_MYSQL = "mysql";
  private static final String STORAGE_MODE_KEY = "storage.mode";

  public static void main(String[] args) {
    final Properties props = loadProperties();
    final String storageMode = props.getProperty(STORAGE_MODE_KEY, "memory");

    final OrderRepository repository = buildRepository(storageMode);
    final OrderService service = new OrderServiceImpl(repository);

    logger.info(() -> "Application started with storage mode: " + storageMode);
  }

  private static OrderRepository buildRepository(String storageMode) {
    if (STORAGE_MODE_MYSQL.equalsIgnoreCase(storageMode)) {
      logger.info(() -> "Using MySqlOrderRepository");
      return new MySqlOrderRepository(DBConnection.getInstance());
    }
    logger.info(() -> "Using InMemoryOrderRepository");
    return new OrderRepositoryImpl();
  }

  private static Properties loadProperties() {
    final Properties props = new Properties();
    try (InputStream input = Main.class.getClassLoader()
        .getResourceAsStream("application.properties")) {
      if (input == null) {
        throw new IllegalStateException("application.properties not found");
      }
      props.load(input);
    } catch (IOException e) {
      throw new IllegalStateException("Failed to load application.properties", e);
    }
    return props;
  }
}