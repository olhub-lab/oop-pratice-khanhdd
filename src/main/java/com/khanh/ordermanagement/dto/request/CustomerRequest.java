package com.khanh.ordermanagement.dto.request;

import com.khanh.ordermanagement.exception.BadRequestException;

public class CustomerRequest {
  private String name;
  private String phone;

  public CustomerRequest(String name, String phone) {
    this.name = name;
    this.phone = phone;
  }

  public String getName() { return name; }
  public String getPhone() { return phone; }

  public void validate() {
    if (this.name == null || this.name.isBlank()) throw new BadRequestException("Name is required");
    if (this.phone == null || this.phone.isBlank()) throw new BadRequestException("Phone is required");
  }
}