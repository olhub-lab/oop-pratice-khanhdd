package exception.database;

import exception.BaseException;

public class ApplicationPropertiesException extends BaseException {

  public ApplicationPropertiesException(String errorCode, String message) {
    super(errorCode, message);
  }
}