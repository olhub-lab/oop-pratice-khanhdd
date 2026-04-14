
import config.DBConnection;
import dao.CustomerDAO;
import dao.OrderDAO;
import dao.PaymentDAO;
import dao.impl.CustomerDAOImpl;
import dao.impl.OrderDAOImpl;
import dao.impl.PaymentDAOImpl;
import service.customer.CustomerService;
import service.customer.impl.CustomerServiceImpl;
import service.order.OrderService;
import service.order.impl.OrderServiceImpl;
import service.payment.PaymentService;
import service.payment.impl.PaymentServiceImpl;

public class Main {

  public static void main(String[] args) {
    try {
      DBConnection.getInstance();
      System.out.println("--- Kết nối Database thành công ---");

      CustomerDAO customerDAO = new CustomerDAOImpl();
      OrderDAO orderDAO = new OrderDAOImpl();
      PaymentDAO paymentDAO = new PaymentDAOImpl();

      CustomerService customerService = new CustomerServiceImpl(customerDAO);
      PaymentService paymentService = new PaymentServiceImpl();
      OrderService orderService = new OrderServiceImpl();

      System.out.println("--- Bắt đầu chạy ứng dụng ---");

      runDemo(customerService, orderService, paymentService);

    } catch (Exception e) {
      System.err.println("Ứng dụng gặp lỗi hệ thống: " + e.getMessage());
      e.printStackTrace();
    }
  }

  private static void runDemo(
      CustomerService customerService,
      OrderService orderService,
      PaymentService paymentService) {

  }
}