package com.khanh.ordermanagement.repository;

import com.khanh.ordermanagement.model.Payment;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, String>{
  List<Payment> findByOrderId(String orderId);
}

