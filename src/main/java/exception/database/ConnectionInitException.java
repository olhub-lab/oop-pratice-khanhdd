package exception.database;

import exception.BaseException;

public class ConnectionInitException extends BaseException {

  public ConnectionInitException(String message, Throwable cause) {
    super("CONNECTION_INIT_ERROR", message, cause);
  }
}