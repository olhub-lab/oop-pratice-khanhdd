package exception;

public class NotFoundException extends BaseException {

  private static final String DEFAULT_CODE = "NOT_FOUND";

  public NotFoundException(final String message) {
    super(DEFAULT_CODE, message);
  }

  public NotFoundException(final String message, final Throwable cause) {
    super(DEFAULT_CODE, message, cause);
  }
}