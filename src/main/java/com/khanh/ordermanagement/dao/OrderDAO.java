package com.khanh.ordermanagement.dao;

import com.khanh.ordermanagement.dto.request.OrderFilterRequest;
import com.khanh.ordermanagement.model.Order;
import java.util.List;
import java.util.Optional;

public interface OrderDAO {
  Order save(Order order);
  void update(Order order);
  Optional<Order> findById(String id);
  List<Order> findAll(OrderFilterRequest filter);
  int count(OrderFilterRequest filter);
}