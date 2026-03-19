package repository;

import java.util.List;
import java.util.Optional;
import model.Order;

public interface OrderRepository {
  void save(Order order);
  Optional<Order> findById(String orderId);
  List<Order> findAll();
  void update(Order order);
}