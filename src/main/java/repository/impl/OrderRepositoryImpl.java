package repository.impl;

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
      logger.info("Attempting to save order: " + order.getOrderId());
      storage.put(order.getOrderId(), order);
      logger.info("Order saved successfully: " + order.getOrderId());
    } catch (Exception e) {
      logger.log(Level.SEVERE, "Failed to save order: " + order.getOrderId(), e);
      throw e;
    }
  }

  @Override
  public Optional<Order> findById(String orderId) {
    logger.info("Searching for orderId: " + orderId);
    Optional<Order> result = Optional.ofNullable(storage.get(orderId));

    if (result.isEmpty()) {
      logger.warning("Order not found in storage: " + orderId);
    }
    return result;
  }

  @Override
  public List<Order> findAll() {
    logger.info("Fetching all orders from storage. Current count: " + storage.size());
    return new ArrayList<>(storage.values());
  }

  @Override
  public void update(Order order) {
    try {
      if (!storage.containsKey(order.getOrderId())) {
        logger.warning("Update failed: Order does not exist: " + order.getOrderId());
      }
      storage.put(order.getOrderId(), order);
      logger.info("Order updated successfully: " + order.getOrderId());
    } catch (Exception e) {
      logger.log(Level.SEVERE, "Error updating order: " + order.getOrderId(), e);
      throw e;
    }
  }
}