package repository.Implement;

import java.util.*;
import model.Order;
import repository.OrderRepository;

public class OrderRepositoryImplement implements OrderRepository {

  private final Map<String, Order> storage = new HashMap<>();

  @Override
  public void save(Order order) {
    storage.put(order.getOrderId(), order);
  }

  @Override
  public Optional<Order> findById(String orderId) {
    return Optional.ofNullable(storage.get(orderId));
  }

  @Override
  public List<Order> findAll() {
    return new ArrayList<>(storage.values());
  }

  @Override
  public void update(Order order) {
    storage.put(order.getOrderId(), order);
  }
}