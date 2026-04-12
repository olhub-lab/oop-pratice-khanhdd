package exception.database;

import exception.BaseException;

public class ApplicationPropertiesException extends BaseException {

  public ApplicationPropertiesException(String message) {
    super("CONFIG_ERROR", message);
  }

  public ApplicationPropertiesException(String message, Throwable cause) {
    super("CONFIG_ERROR", message, cause);
  }
}