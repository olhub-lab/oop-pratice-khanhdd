package exception;

public class NotFoundException extends BaseException {

  public NotFoundException(String message) {
    super("NOT_FOUND", message);
  }

  public NotFoundException(String entityName, Object id) {
    super("NOT_FOUND", String.format("%s với ID [%s] không tồn tại", entityName, id));
  }
}