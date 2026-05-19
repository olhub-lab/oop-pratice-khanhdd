package com.khanh.ordermanagement.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "customers")
public class Customer {

  @Id
  @Column(name = "id", nullable = false)
  private String id;

  @Column(name = "name",  nullable = false)
  private String name;

  @Column(name = "phone", nullable = false, unique = true)
  private String phone;

  @Column(name = "created_at")
  private LocalDateTime createdAt;

  public Customer() {
  }

  public Customer(String id, String name, String phone) {
    this.id = id;
    this.name = name;
    this.phone = phone;
    this.createdAt = LocalDateTime.now();
  }

  public Customer(String id, String name, String phone, LocalDateTime createdAt) {
    this.id = id;
    this.name = name;
    this.phone = phone;
    this.createdAt = createdAt;
  }

  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getPhone() {
    return phone;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  @Override
  public String toString() {
    return "Customer{" +
        "id=" + id +
        ", name='" + name + '\'' +
        ", phone='" + phone + '\'' +
        ", createdAt=" + createdAt +
        '}';
  }
}