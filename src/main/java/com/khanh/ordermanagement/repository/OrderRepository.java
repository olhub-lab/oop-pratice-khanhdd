package com.khanh.ordermanagement.repository;

import com.khanh.ordermanagement.model.Order;
import java.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {
  @Query("SELECT o FROM Order o WHERE " +
      "(:status IS NULL OR o.status = :status) AND " +
      "(:customerId IS NULL OR o.customerId = :customerId) AND " +
      "(:fromDate IS NULL OR o.createdAt >= :fromDate) AND " +
      "(:toDate IS NULL OR o.createdAt <= :toDate)")
  Page<Order> findWithFilter(
      @Param("status") Object status,
      @Param("customerId") String customerId,
      @Param("fromDate") LocalDateTime fromDate,
      @Param("toDate") LocalDateTime toDate,
      Pageable pageable
  );
}
