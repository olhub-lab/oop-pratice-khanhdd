package exception.database;

import exception.BaseException;

public class DuplicateRecordException extends BaseException {
  public DuplicateRecordException(String message) {
    super(message, "DUPLICATE_RECORD");
  }
}