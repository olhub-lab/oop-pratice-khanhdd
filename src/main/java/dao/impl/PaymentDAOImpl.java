package dao.impl;

import dao.PaymentDAO;
import model.Payment;
import java.util.Optional;

public class PaymentDAOImpl implements PaymentDAO {

  @Override
  public Payment save(Payment payment) {
    return null;
  }

  @Override
  public Optional<Payment> findById(String id) {
    return Optional.empty();
  }

  @Override
  public Optional<Payment> findByOrderId(String orderId) {
    return Optional.empty();
  }

  @Override
  public void update(Payment payment) {
  }
}