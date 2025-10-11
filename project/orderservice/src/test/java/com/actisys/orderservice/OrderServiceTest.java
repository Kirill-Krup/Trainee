package com.actisys.orderservice;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.actisys.orderservice.dto.OrderDTO;
import com.actisys.orderservice.dto.UserDTO;
import com.actisys.orderservice.exception.OrderNotFoundException;
import com.actisys.orderservice.mapper.OrderMapper;
import com.actisys.orderservice.model.Order;
import com.actisys.orderservice.model.enumClasses.StatusType;
import com.actisys.orderservice.repository.OrderRepository;
import com.actisys.orderservice.service.impl.OrderServiceImpl;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

  @Mock
  private OrderRepository repository;

  @Mock
  private OrderMapper mapper;

  @InjectMocks
  private OrderServiceImpl orderService;

  @Test
  @DisplayName("This test should create order")
  void createOrder() {
    OrderDTO inputDTO = createOrderDTO(null, StatusType.PENDING);
    Order orderEntity = createOrderEntity(null, StatusType.PENDING);
    Order savedOrder = createOrderEntity(1L, StatusType.PENDING);
    OrderDTO expectedDTO = createOrderDTO(1L, StatusType.PENDING);

    when(mapper.toEntity(inputDTO)).thenReturn(orderEntity);
    when(repository.save(orderEntity)).thenReturn(savedOrder);
    when(mapper.toDTO(savedOrder)).thenReturn(expectedDTO);

    OrderDTO result = orderService.createOrder(inputDTO);

    assertThat(result.getId()).isEqualTo(1L);
    assertThat(result.getStatus()).isEqualTo(StatusType.PENDING);
    verify(mapper).toEntity(inputDTO);
    verify(repository).save(orderEntity);
    verify(mapper).toDTO(savedOrder);
  }

  @Test
  @DisplayName("This test should get order by id")
  void getOrderById() {
    Long orderId = 1L;
    Order orderEntity = createOrderEntity(orderId, StatusType.CONFIRMED);
    OrderDTO expectedDTO = createOrderDTO(orderId, StatusType.CONFIRMED);

    when(repository.findById(orderId)).thenReturn(Optional.of(orderEntity));
    when(mapper.toDTO(orderEntity)).thenReturn(expectedDTO);

    Optional<OrderDTO> result = orderService.getOrderById(orderId);

    assertThat(result).isPresent();
    assertThat(result.get().getId()).isEqualTo(orderId);
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
    verify(mapper, never()).toDTO((Order) any());
  }

  @Test
  @DisplayName("This test should get orders by ids")
  void getOrdersByIds() {
    List<Long> orderIds = List.of(1L, 2L);
    Order order1 = createOrderEntity(1L, StatusType.PENDING);
    Order order2 = createOrderEntity(2L, StatusType.CONFIRMED);
    OrderDTO dto1 = createOrderDTO(1L, StatusType.PENDING);
    OrderDTO dto2 = createOrderDTO(2L, StatusType.CONFIRMED);

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
    OrderDTO dto1 = createOrderDTO(1L, StatusType.PENDING);
    OrderDTO dto2 = createOrderDTO(2L, StatusType.CONFIRMED);

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
    OrderDTO updateDTO = createOrderDTO(null, StatusType.SHIPPED);
    Order existingOrder = createOrderEntity(orderId, StatusType.PENDING);
    Order updatedOrder = createOrderEntity(orderId, StatusType.SHIPPED);
    OrderDTO expectedDTO = createOrderDTO(orderId, StatusType.SHIPPED);

    when(repository.findById(orderId)).thenReturn(Optional.of(existingOrder));
    when(repository.save(existingOrder)).thenReturn(updatedOrder);
    when(mapper.toDTO(updatedOrder)).thenReturn(expectedDTO);

    OrderDTO result = orderService.updateOrder(orderId, updateDTO);

    assertThat(result.getId()).isEqualTo(orderId);
    assertThat(result.getStatus()).isEqualTo(StatusType.SHIPPED);
    verify(repository).findById(orderId);
    verify(repository).save(existingOrder);
    assertThat(existingOrder.getStatus()).isEqualTo(StatusType.SHIPPED);
  }

  @Test
  @DisplayName("This test should throw custom exception, because no order to update")
  void updateNotExistedOrder() {

    Long orderId = 999L;
    OrderDTO updateDTO = createOrderDTO(null, StatusType.SHIPPED);

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


  private OrderDTO createOrderDTO(Long id, StatusType status) {
    UserDTO userDTO = new UserDTO(1L, "Kirill", "Krupenin", null, "kiryl.krupenin@innowise.com");
    return new OrderDTO(id, userDTO, status, LocalDateTime.now());
  }

  private Order createOrderEntity(Long id, StatusType status) {
    Order order = new Order();
    order.setId(id);
    order.setUserId(1L);
    order.setStatus(status);
    order.setCreationDate(LocalDateTime.now());
    return order;
  }
}