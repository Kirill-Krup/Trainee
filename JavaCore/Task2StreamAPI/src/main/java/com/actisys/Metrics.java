package com.actisys;

import com.actisys.EnumClasses.OrderStatus;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class Metrics {

  public List<String> getUniqueCities(List<Order> allOrders) {
    List<String> uniqueCities = allOrders.stream().collect(Collectors.groupingBy(element ->
            element.getCustomer().getCity(), Collectors.counting()
        )).entrySet().stream().filter(entry -> entry.getValue() == 1).map(Map.Entry::getKey)
        .collect(Collectors.toList());
    return uniqueCities;
  }

  public double getTotalIncomeForAllCompletedOrders(List<Order> allOrders) {
    return allOrders.stream().filter(order -> order.getStatus() == OrderStatus.DELIVERED)
        .flatMap(order -> order.getItems().stream())
        .mapToDouble(item -> item.getPrice() * item.getQuantity()).sum();
  }

  public double getAverageCheckForDeliveredOrders(List<Order> allOrders) {
    List<Order> completedOrders = allOrders.stream()
        .filter(order -> order.getStatus() == OrderStatus.DELIVERED).collect(Collectors.toList());
    if (completedOrders.isEmpty()) {
      return 0;
    }
    double totalIncome = getTotalIncomeForAllCompletedOrders(completedOrders);
    return totalIncome / completedOrders.size();
  }

  public String getTheMostPopularProduct(List<Order> allOrders) {
    String popularItem = allOrders.stream().flatMap(el -> el.getItems().stream())
        .collect(Collectors.groupingBy(OrderItem::getProductName,
            Collectors.summingInt(OrderItem::getQuantity)
        )).entrySet().stream().max(Map.Entry.comparingByValue()).map(Map.Entry::getKey)
        .orElse("No products");
    return popularItem;
  }

  public List<Customer> getCustomersWithFivePlusOrders(List<Order> allOrders) {
    List<Customer> fivePlusProductsCustomers = allOrders.stream()
        .collect(Collectors.groupingBy(element ->
            element.getCustomer(), Collectors.counting())).entrySet().stream().
        filter(entry -> entry.getValue() > 5).map(Map.Entry::getKey).collect(Collectors.toList());

    return fivePlusProductsCustomers;
  }
}

