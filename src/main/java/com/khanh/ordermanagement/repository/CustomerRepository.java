package com.khanh.ordermanagement.repository;

import com.khanh.ordermanagement.entity.Customer;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, String> {
  Optional<Customer> findByPhone(String phone);
}
