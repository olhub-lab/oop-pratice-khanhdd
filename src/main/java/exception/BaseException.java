package exception;

public abstract class BaseException extends RuntimeException {
  private final String errorCode;
  private final String message;

  protected BaseException(final String errorCode, final String message) {
    super(message);
    this.errorCode = errorCode;
    this.message = message;

  }
  protected BaseException(final String errorCode, final String message, final Throwable cause) {
    super(message, cause);
    this.errorCode = errorCode;
    this.message = message;
  }

  public String getErrorCode() {
    return errorCode;
  }

  @Override
  public String getMessage() {
    return message;
  }
}