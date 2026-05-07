package com.khanh.ordermanagement.exception.database;

import com.khanh.ordermanagement.exception.BaseException;

public class DuplicateRecordException extends BaseException {
  public DuplicateRecordException(String message) {
    super(message, "DUPLICATE_RECORD");
  }
}