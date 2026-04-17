package dao;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;
import model.Order;
import dto.request.OrderFilterRequest;

public interface OrderDAO {

  Order save(Connection conn, Order order);

  Optional<Order> findById(String id);

  List<Order> findAll(OrderFilterRequest filter);

  void update(Connection conn, Order order);
}