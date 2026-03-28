package exception.database;

import exception.BaseException;

public class ConnectionInitException extends BaseException {

  public ConnectionInitException(String errorCode, String message, Throwable cause) {
    super(errorCode, message, cause);
  }
}