package exception;

public class BusinessException extends BaseException {

  private static final String DEFAULT_CODE = "BUSINESS_ERROR";

  public BusinessException(final String message) {
    super(DEFAULT_CODE, message);
  }

  public BusinessException(final String message, final Throwable cause) {
    super(DEFAULT_CODE, message, cause);
  }
}