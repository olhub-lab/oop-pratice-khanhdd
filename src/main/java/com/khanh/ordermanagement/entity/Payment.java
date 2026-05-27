package com.khanh.ordermanagement.entity;

import java.math.BigDecimal;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
import com.khanh.ordermanagement.entity.enums.PaymentMethod;
import com.khanh.ordermanagement.entity.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "payments")
public class Payment {

  @Id
  @Column(name = "id")
  private String id;

  @Column(name = "order_id", nullable = false)
  private String orderId;

  @Column(name = "final_amount", nullable = false)
  private BigDecimal finalAmount;

  @Enumerated(EnumType.STRING)
  @Column(name = "method")
  private PaymentMethod method;

  @Column(name = "created_at")
  private LocalDateTime createdAt;

  @Enumerated(EnumType.STRING)
  @Column(name = "status")
  private PaymentStatus status;

  public Payment(String orderId, BigDecimal finalAmount, PaymentMethod method,
      PaymentStatus status) {
    this.id = UUID.randomUUID().toString();
    this.orderId = orderId;
    this.finalAmount = finalAmount;
    this.method = method;
    this.createdAt = LocalDateTime.now();
    this.status = status;
  }
}