package exception.database;

import exception.BaseException;

public class ServiceException extends BaseException {
  public ServiceException(String message, Throwable cause) {
    super(message, "INTERNAL_SERVICE_ERROR", cause);
  }
}