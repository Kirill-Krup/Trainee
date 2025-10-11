package com.actisys.orderservice.service.impl;

import com.actisys.orderservice.dto.OrderDTO;
import com.actisys.orderservice.exception.OrderNotFoundException;
import com.actisys.orderservice.mapper.OrderMapper;
import com.actisys.orderservice.model.Order;
import com.actisys.orderservice.model.enumClasses.StatusType;
import com.actisys.orderservice.repository.OrderRepository;
import com.actisys.orderservice.service.OrderService;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

  private final OrderRepository repository;
  private final OrderMapper mapper;

  @Override
  public OrderDTO createOrder(OrderDTO order) {
    Order orderForSave = mapper.toEntity(order);
    Order savedEntity = repository.save(orderForSave);
    return mapper.toDTO(savedEntity);
  }

  @Override
  public Optional<OrderDTO> getOrderById(Long orderId) {
    Order order = repository.findById(orderId).orElseThrow(()-> new OrderNotFoundException(orderId) );
    return Optional.ofNullable(mapper.toDTO(order));
  }

  @Override
  public List<OrderDTO> getOrdersByIdIn(List<Long> orderIds) {
    return repository.findByIdIn(orderIds).stream().map(mapper::toDTO).collect(Collectors.toList());
  }

  @Override
  public List<OrderDTO> getOrdersByStatusIn(List<StatusType> statuses) {
    return repository.findByStatusIn(statuses).stream().map(mapper::toDTO).collect(Collectors.toList());
  }

  @Override
  @Transactional
  public OrderDTO updateOrder(Long id, OrderDTO order) {
    Order existingOrder = repository.findById(id).orElseThrow(() -> new OrderNotFoundException(id));
    existingOrder.setStatus(order.getStatus());
    Order savedEntity = repository.save(existingOrder);
    return mapper.toDTO(savedEntity);
  }

  @Override
  @Transactional
  public void deleteOrder(Long orderId) {
    if(!repository.existsById(orderId)) {
      throw new OrderNotFoundException(orderId);
    }
    repository.deleteById(orderId);
  }
}
