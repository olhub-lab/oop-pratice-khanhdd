package exception.database;

import exception.BaseException;

public class DatabaseException extends BaseException {

  public DatabaseException(String message) {
    super("DB_500", message);
  }

  public DatabaseException(String message, Throwable cause) {
    super("DB_500", message, cause);
  }

  public DatabaseException(String errorCode, String message, Throwable cause) {
    super(errorCode, message, cause);
  }
}