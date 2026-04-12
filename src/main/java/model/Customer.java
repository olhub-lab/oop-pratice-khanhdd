package model;

import java.time.LocalDateTime;

public class Customer {

  private final Long id;
  private String name;
  private String phone;
  private final LocalDateTime createdAt;

  public Customer(Long id, String name, String phone) {
    this.id = id;
    this.name = name;
    this.phone = phone;
    this.createdAt = LocalDateTime.now();
  }

  public Customer(Long id, String name, String phone, LocalDateTime createdAt) {
    this.id = id;
    this.name = name;
    this.phone = phone;
    this.createdAt = createdAt;
  }

  public Long getId() {
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