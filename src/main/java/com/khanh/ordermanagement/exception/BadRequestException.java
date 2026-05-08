package com.khanh.ordermanagement.exception;

public class BadRequestException extends BaseException {

  public BadRequestException(String message) {
    super("BAD_REQUEST", message);
  }

  public BadRequestException(String errorCode, String message) {
    super(errorCode, message);
  }
}