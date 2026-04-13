package service.customer;

import dto.request.CustomerRequest;
import dto.response.CustomerResponse;
import java.util.List;

public interface CustomerService {

  CustomerResponse create(CustomerRequest request);

  CustomerResponse getById(Long id);

  CustomerResponse getByPhone(String phone);

  List<CustomerResponse> getAll();
}