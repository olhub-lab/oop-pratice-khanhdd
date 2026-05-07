package com.khanh.ordermanagement.exception.database;

import com.khanh.ordermanagement.exception.BaseException;

public class ConnectionInitException extends BaseException {

  public ConnectionInitException(String message, Throwable cause) {
    super("CONNECTION_INIT_ERROR", message, cause);
  }
}