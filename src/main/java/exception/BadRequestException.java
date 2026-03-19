package exception;

public class BadRequestException extends BaseException {

  private static final String DEFAULT_CODE = "BAD_REQUEST";

  public BadRequestException(final String message) {
    super(DEFAULT_CODE, message);
  }

  public BadRequestException(final String message, final Throwable cause) {
    super(DEFAULT_CODE, message, cause);
  }
}