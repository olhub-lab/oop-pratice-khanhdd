package exception.database;

import exception.BaseException;

public class DatabaseException extends BaseException {

  private static final String DEFAULT_CODE = "DB_ERROR";

  public DatabaseException(String message, Throwable cause) {
    super(DEFAULT_CODE, message, cause);
  }
}