package dto.response;

import java.time.LocalDateTime;

public class CustomerResponse {
  private final Long id;
  private final String name;
  private final String phone;
  private final LocalDateTime createdAt;

  public CustomerResponse(Long id, String name, String phone, LocalDateTime createdAt) {
    this.id = id;
    this.name = name;
    this.phone = phone;
    this.createdAt = createdAt;
  }

  public Long getId() {
    return id;
  }

  public String getPhone() {
    return phone;
  }

  public String getName() {
    return name;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }
}