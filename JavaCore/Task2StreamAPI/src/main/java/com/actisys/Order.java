package com.actisys;

import com.actisys.EnumClasses.OrderStatus;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class Order {

  private String orderId;
  private LocalDateTime orderDate;
  private Customer customer;
  private List<OrderItem> items;
  private OrderStatus status;

  public String getOrderId() {
    return orderId;
  }

  public void setOrderId(String orderId) {
    this.orderId = orderId;
  }

  public LocalDateTime getOrderDate() {
    return orderDate;
  }

  public void setOrderDate(LocalDateTime orderDate) {
    this.orderDate = orderDate;
  }

  public Customer getCustomer() {
    return customer;
  }

  public void setCustomer(Customer customer) {
    this.customer = customer;
  }

  public List<OrderItem> getItems() {
    return items;
  }

  public void setItems(List<OrderItem> items) {
    this.items = items;
  }

  public OrderStatus getStatus() {
    return status;
  }

  public void setStatus(OrderStatus status) {
    this.status = status;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    Order order = (Order) obj;
    return Objects.equals(orderId, order.orderId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(orderId);
  }
}
