package com.khanh.ordermanagement.facade.order;

import com.khanh.ordermanagement.dto.request.OrderRequest;
import com.khanh.ordermanagement.dto.response.OrderResponse;

public interface OrderFacadeService {

  OrderResponse create(OrderRequest request);

}
