package com.khanh.ordermanagement.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "customers")
public class Customer {

  @Id
  @Column(name = "id", nullable = false)
  private String id;

  @Column(name = "name", nullable = false)
  private String name;

  @Column(name = "phone", nullable = false, unique = true)
  private String phone;

  @Column(name = "created_at")
  private LocalDateTime createdAt;

  public Customer(String id, String name, String phone) {
    this.id = id;
    this.name = name;
    this.phone = phone;
    this.createdAt = LocalDateTime.now();
  }

  @Override
  public String toString() {
    return "Customer{" + "id=" + id + ", name='" + name + '\'' + ", phone='" + phone + '\''
        + ", createdAt=" + createdAt + '}';
  }
}