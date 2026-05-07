package com.khanh.ordermanagement.dao;

import java.util.Optional;
import com.khanh.ordermanagement.model.Payment;

public interface PaymentDAO {

  Payment save(Payment payment);

  Optional<Payment> findById(String id);

  Optional<Payment> findByOrderId(String orderId);

  void update(Payment payment);
}