import java.math.BigDecimal;
import java.util.logging.Logger;
import repository.OrderRepository;
import repository.memory.OrderRepositoryImpl;
import service.order.OrderService;
import service.order.impl.OrderServiceImpl;

public class Main {

  public static void main(String[] args) {
    OrderRepository repository = new OrderRepositoryImpl();
    OrderService service = new OrderServiceImpl(repository);
  }
}