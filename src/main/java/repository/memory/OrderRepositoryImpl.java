package repository.memory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Order;
import repository.OrderRepository;

public class OrderRepositoryImpl implements OrderRepository {

  private static final Logger logger = Logger.getLogger(OrderRepositoryImpl.class.getName());
  private final Map<String, Order> storage = new HashMap<>();

  @Override
  public void save(Order order) {
    try {
      logger.info(() -> "Attempting to save order: " + order.getOrder());
      storage.put(order.getOrder(), order);
      logger.info(() -> "Order saved successfully: " + order.getOrder());
    } catch (Exception e) {
      logger.log(Level.SEVERE, e, () -> "Failed to save order: " + order.getOrder());
      throw e;
    }
  }

  @Override
  public Optional<Order> findById(String orderId) {
    logger.info(() -> "Searching for orderId: " + orderId);
    Optional<Order> result = Optional.ofNullable(storage.get(orderId));

    if (result.isEmpty()) {
      logger.warning(() -> "Order not found in storage: " + orderId);
    }
    return result;
  }

  @Override
  public List<Order> findAll() {
    logger.info(() -> "Fetching all orders from storage. Current count: " + storage.size());
    return new ArrayList<>(storage.values());
  }

  @Override
  public void update(Order order) {
    try {
      if (!storage.containsKey(order.getOrder())) {
        logger.warning(() -> "Update failed: Order does not exist: " + order.getOrder());
      }
      storage.put(order.getOrder(), order);
      logger.info(() -> "Order updated successfully: " + order.getOrder());
    } catch (Exception e) {
      logger.log(Level.SEVERE, e, () -> "Error updating order: " + order.getOrder());
      throw e;
    }
  }
}