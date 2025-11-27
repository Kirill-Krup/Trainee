package com.actisys.orderservice.service;

import com.actisys.orderservice.dto.OrderDTO;
import com.actisys.orderservice.model.enumClasses.StatusType;
import java.util.List;
import java.util.Optional;

public interface OrderService {

  OrderDTO createOrder(OrderDTO order, String email);

  Optional<OrderDTO> getOrderById(Long orderId);

  List<OrderDTO> getOrdersByIdIn(List<Long> orderIds);

  List<OrderDTO> getOrdersByStatusIn(List<StatusType> statuses);

  OrderDTO updateOrder(Long id, OrderDTO order);

  void deleteOrder(Long orderId);

  void updateOrderStatus(Long orderId, StatusType status);
}
