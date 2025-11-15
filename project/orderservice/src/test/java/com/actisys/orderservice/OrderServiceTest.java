package com.actisys.orderservice;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.actisys.orderservice.dto.OrderDTO;
import com.actisys.orderservice.dto.event.CreateOrderEvent;
import com.actisys.orderservice.exception.OrderNotFoundException;
import com.actisys.orderservice.fallback.UserServiceClient;
import com.actisys.orderservice.mapper.OrderMapper;
import com.actisys.orderservice.model.Order;
import com.actisys.orderservice.model.enumClasses.StatusType;
import com.actisys.orderservice.repository.OrderRepository;
import com.actisys.orderservice.service.impl.OrderServiceImpl;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

  @Mock
  private OrderRepository repository;

  @Mock
  private OrderMapper mapper;

  @Mock
  private UserServiceClient userServiceClient;

  @Mock
  private KafkaTemplate<String, Object> kafkaTemplate;

  @InjectMocks
  private OrderServiceImpl orderService;

  @Test
  @DisplayName("This test should create order with user ID from email")
  void createOrder() {
    String email = "user@example.com";
    Long userId = 1L;
    OrderDTO inputDTO = new OrderDTO(null, null, StatusType.PENDING, LocalDateTime.now(),
        Collections.emptyList());
    Order orderEntity = createOrderEntity(null, StatusType.PENDING);
    Order savedOrder = createOrderEntity(1L, StatusType.PENDING);
    OrderDTO expectedDTO = new OrderDTO(1L, userId, StatusType.PENDING, LocalDateTime.now(),
        Collections.emptyList());

    when(userServiceClient.getUserIdByEmail(email)).thenReturn(1L);
    when(mapper.toEntity(any(OrderDTO.class))).thenReturn(orderEntity);
    when(repository.save(orderEntity)).thenReturn(savedOrder);
    when(mapper.toDTO(savedOrder)).thenReturn(expectedDTO);
    CompletableFuture<SendResult<String, Object>> future = CompletableFuture.completedFuture(null);
    when(kafkaTemplate.send(anyString(), any())).thenReturn(future);

    OrderDTO result = orderService.createOrder(inputDTO, email);

    assertThat(result.getId()).isEqualTo(1L);
    assertThat(result.getUserId()).isEqualTo(userId);
    assertThat(result.getStatus()).isEqualTo(StatusType.PENDING);

    verify(userServiceClient).getUserIdByEmail(email);
    verify(mapper).toEntity(any(OrderDTO.class));
    verify(repository).save(orderEntity);
    verify(mapper).toDTO(savedOrder);
    verify(kafkaTemplate).send(eq("CREATE_ORDER"), any(CreateOrderEvent.class));
  }

  @Test
  @DisplayName("This test should throw exception when user not found by email")
  void createOrder_userNotFound() {
    String email = "nonexistent@example.com";
    OrderDTO inputDTO = new OrderDTO(null, null, StatusType.PENDING, LocalDateTime.now(), null);

    when(userServiceClient.getUserIdByEmail(email)).thenReturn(null);

    assertThatThrownBy(() -> orderService.createOrder(inputDTO, email))
        .isInstanceOf(RuntimeException.class)
        .hasMessageContaining("User " + email + " not found");

    verify(userServiceClient).getUserIdByEmail(email);
    verify(mapper, never()).toEntity(any());
    verify(repository, never()).save(any());
  }

  @Test
  @DisplayName("This test should get order by id")
  void getOrderById() {
    Long orderId = 1L;
    Long userId = 1L;
    Order orderEntity = createOrderEntity(orderId, StatusType.CONFIRMED);
    OrderDTO expectedDTO = new OrderDTO(orderId, userId, StatusType.CONFIRMED, LocalDateTime.now(), null);

    when(repository.findById(orderId)).thenReturn(Optional.of(orderEntity));
    when(mapper.toDTO(orderEntity)).thenReturn(expectedDTO);

    Optional<OrderDTO> result = orderService.getOrderById(orderId);

    assertThat(result).isPresent();
    assertThat(result.get().getId()).isEqualTo(orderId);
    assertThat(result.get().getUserId()).isEqualTo(userId);
    assertThat(result.get().getStatus()).isEqualTo(StatusType.CONFIRMED);
    verify(repository).findById(orderId);
    verify(mapper).toDTO(orderEntity);
  }

  @Test
  @DisplayName("This test should throw my custom exception 'OrderNotFound'")
  void orderNotFound() {
    Long orderId = 999L;
    when(repository.findById(orderId)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> orderService.getOrderById(orderId))
        .isInstanceOf(OrderNotFoundException.class)
        .hasMessageContaining("999");

    verify(repository).findById(orderId);
    verify(mapper, never()).toDTO(any(Order.class));
  }

  @Test
  @DisplayName("This test should get orders by ids")
  void getOrdersByIds() {
    List<Long> orderIds = List.of(1L, 2L);
    Order order1 = createOrderEntity(1L, StatusType.PENDING);
    Order order2 = createOrderEntity(2L, StatusType.CONFIRMED);
    OrderDTO dto1 = new OrderDTO(1L, 1L, StatusType.PENDING, LocalDateTime.now(), null);
    OrderDTO dto2 = new OrderDTO(2L, 2L, StatusType.CONFIRMED, LocalDateTime.now(), null);

    when(repository.findByIdIn(orderIds)).thenReturn(List.of(order1, order2));
    when(mapper.toDTO(order1)).thenReturn(dto1);
    when(mapper.toDTO(order2)).thenReturn(dto2);

    List<OrderDTO> result = orderService.getOrdersByIdIn(orderIds);

    assertThat(result).hasSize(2);
    assertThat(result.get(0).getId()).isEqualTo(1L);
    assertThat(result.get(1).getId()).isEqualTo(2L);
    verify(repository).findByIdIn(orderIds);
    verify(mapper, times(2)).toDTO(any(Order.class));
  }

  @Test
  @DisplayName("This test should get orders by statuses")
  void getOrdersByStatuses() {
    List<StatusType> statuses = List.of(StatusType.PENDING, StatusType.CONFIRMED);
    Order order1 = createOrderEntity(1L, StatusType.PENDING);
    Order order2 = createOrderEntity(2L, StatusType.CONFIRMED);
    OrderDTO dto1 = new OrderDTO(1L, 1L, StatusType.PENDING, LocalDateTime.now(), null);
    OrderDTO dto2 = new OrderDTO(2L, 2L, StatusType.CONFIRMED, LocalDateTime.now(), null);

    when(repository.findByStatusIn(statuses)).thenReturn(List.of(order1, order2));
    when(mapper.toDTO(order1)).thenReturn(dto1);
    when(mapper.toDTO(order2)).thenReturn(dto2);

    List<OrderDTO> result = orderService.getOrdersByStatusIn(statuses);

    assertThat(result).hasSize(2);
    assertThat(result.get(0).getStatus()).isEqualTo(StatusType.PENDING);
    assertThat(result.get(1).getStatus()).isEqualTo(StatusType.CONFIRMED);
    verify(repository).findByStatusIn(statuses);
  }

  @Test
  @DisplayName("This test should update order")
  void updateOrder() {
    Long orderId = 1L;
    Long userId = 1L;
    OrderDTO updateDTO = new OrderDTO(null, userId, StatusType.SHIPPED, LocalDateTime.now(), null);
    Order existingOrder = createOrderEntity(orderId, StatusType.PENDING);
    Order updatedOrder = createOrderEntity(orderId, StatusType.SHIPPED);
    OrderDTO expectedDTO = new OrderDTO(orderId, userId, StatusType.SHIPPED, LocalDateTime.now(), null);

    when(repository.findById(orderId)).thenReturn(Optional.of(existingOrder));
    when(repository.save(existingOrder)).thenReturn(updatedOrder);
    when(mapper.toDTO(updatedOrder)).thenReturn(expectedDTO);

    OrderDTO result = orderService.updateOrder(orderId, updateDTO);

    assertThat(result.getId()).isEqualTo(orderId);
    assertThat(result.getUserId()).isEqualTo(userId);
    assertThat(result.getStatus()).isEqualTo(StatusType.SHIPPED);
    verify(repository).findById(orderId);
    verify(repository).save(existingOrder);
    assertThat(existingOrder.getStatus()).isEqualTo(StatusType.SHIPPED);
  }

  @Test
  @DisplayName("This test should throw custom exception, because no order to update")
  void updateNotExistedOrder() {
    Long orderId = 999L;
    OrderDTO updateDTO = new OrderDTO(null, 1L, StatusType.SHIPPED, LocalDateTime.now(), null);

    when(repository.findById(orderId)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> orderService.updateOrder(orderId, updateDTO))
        .isInstanceOf(OrderNotFoundException.class)
        .hasMessageContaining("999");

    verify(repository).findById(orderId);
    verify(repository, never()).save(any());
  }

  @Test
  @DisplayName("This test should delete order")
  void deleteOrder() {
    Long orderId = 1L;
    when(repository.existsById(orderId)).thenReturn(true);

    orderService.deleteOrder(orderId);

    verify(repository).existsById(orderId);
    verify(repository).deleteById(orderId);
  }

  @Test
  @DisplayName("This test should throw custom exception. because order not found")
  void deleteNotExistedOrder() {
    Long orderId = 999L;
    when(repository.existsById(orderId)).thenReturn(false);

    assertThatThrownBy(() -> orderService.deleteOrder(orderId))
        .isInstanceOf(OrderNotFoundException.class)
        .hasMessageContaining("999");

    verify(repository).existsById(orderId);
    verify(repository, never()).deleteById(orderId);
  }

  private Order createOrderEntity(Long id, StatusType status) {
    Order order = new Order();
    order.setId(id);
    order.setUserId(1L);
    order.setStatus(status);
    order.setCreationDate(LocalDateTime.now());
    order.setOrderItems(Collections.emptyList());
    return order;
  }

}