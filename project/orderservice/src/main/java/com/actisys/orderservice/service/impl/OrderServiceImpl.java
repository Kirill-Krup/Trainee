package com.actisys.orderservice.service.impl;

import com.actisys.orderservice.dto.OrderDTO;
import com.actisys.orderservice.dto.event.CreateOrderEvent;
import com.actisys.orderservice.exception.OrderNotFoundException;
import com.actisys.orderservice.exception.UserNotFoundException;
import com.actisys.orderservice.fallback.UserServiceClient;
import com.actisys.orderservice.mapper.OrderMapper;
import com.actisys.orderservice.model.Order;
import com.actisys.orderservice.model.OrderItem;
import com.actisys.orderservice.model.enumClasses.StatusType;
import com.actisys.orderservice.repository.OrderRepository;
import com.actisys.orderservice.service.OrderService;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {

  private final OrderRepository orderRepository;
  private final OrderMapper mapper;
  private final UserServiceClient userServiceClient;
  private final KafkaTemplate<String, Object> kafkaTemplate;

  @Override
  public OrderDTO createOrder(OrderDTO order, String email) {
    Long userId = userServiceClient.getUserIdByEmail(email);
    if(userId == null) {
      throw new UserNotFoundException(email);
    }
    order.setUserId(userId);
    Order orderForSave = mapper.toEntity(order);
    Order savedEntity = orderRepository.save(orderForSave);
    CreateOrderEvent event = new CreateOrderEvent(
        savedEntity.getId(),
        userId,
        calculatePrice(savedEntity.getOrderItems()));
    kafkaTemplate.send("CREATE_ORDER", event);
    log.debug("Kafka event sent to CREATE_ORDER");
    return mapper.toDTO(savedEntity);
  }

  @Override
  public Optional<OrderDTO> getOrderById(Long orderId) {
    Order order = orderRepository.findById(orderId).orElseThrow(()-> new OrderNotFoundException(orderId) );
    return Optional.ofNullable(mapper.toDTO(order));
  }

  @Override
  public List<OrderDTO> getOrdersByIdIn(List<Long> orderIds) {
    return orderRepository.findByIdIn(orderIds).stream().map(mapper::toDTO).collect(Collectors.toList());
  }

  @Override
  public List<OrderDTO> getOrdersByStatusIn(List<StatusType> statuses) {
    return orderRepository.findByStatusIn(statuses).stream().map(mapper::toDTO).collect(Collectors.toList());
  }

  @Override
  @Transactional
  public OrderDTO updateOrder(Long id, OrderDTO order) {
    Order existingOrder = orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException(id));
    existingOrder.setStatus(order.getStatus());
    Order savedEntity = orderRepository.save(existingOrder);
    return mapper.toDTO(savedEntity);
  }

  @Override
  @Transactional
  public void deleteOrder(Long orderId) {
    if(!orderRepository.existsById(orderId)) {
      throw new OrderNotFoundException(orderId);
    }
    orderRepository.deleteById(orderId);
  }

  @Override
  public void updateOrderStatus(Long orderId, StatusType status) {
    Order order = orderRepository.findById(orderId).orElseThrow(() -> new OrderNotFoundException(orderId));
    order.setStatus(status);
    orderRepository.save(order);
  }

  private BigDecimal calculatePrice(List<OrderItem> orderItems){
    return orderItems.stream()
        .map(item->item.getItem().getPrice()
            .multiply(BigDecimal.valueOf(item.getQuantity())))
        .reduce(BigDecimal.ZERO, BigDecimal::add);
  }
}
