package com.actisys;

import com.actisys.EnumClasses.OrderStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MetricsTest {

  private Metrics metricsService = new Metrics();

  private Customer createCustomer(String name, String city) {
    Customer customer = new Customer();
    customer.setName(name);
    customer.setCity(city);
    return customer;
  }

  private OrderItem createItem(String product, double price, int quantity) {
    OrderItem item = new OrderItem();
    item.setProductName(product);
    item.setPrice(price);
    item.setQuantity(quantity);
    return item;
  }

  private Order createOrder(Customer customer, OrderStatus status, OrderItem... items) {
    Order order = new Order();
    order.setCustomer(customer);
    order.setStatus(status);
    order.setItems(List.of(items));
    return order;
  }

  @Test
  @DisplayName("Should return only unique cities from orders list")
  void testGetUniqueCities() {
    List<Order> orders = new ArrayList<>();
    Customer customer1 = createCustomer("Kirill", "Minsk");
    Customer customer2 = createCustomer("Alice", "Minsk");
    Customer customer3 = createCustomer("Anna", "Gomel");
    orders.add(createOrder(customer1, OrderStatus.DELIVERED, createItem("Laptop", 1000, 1)));
    orders.add(createOrder(customer2, OrderStatus.DELIVERED, createItem("Phone", 500, 1)));
    orders.add(createOrder(customer3, OrderStatus.PROCESSING, createItem("Tablet", 300, 1)));
    List<String> result = metricsService.getUniqueCities(orders);
    assertEquals(1, result.size());
    assertTrue(result.contains("Gomel"));
  }

  @Test
  @DisplayName("Should calculate total income only for delivered orders")
  void testGetTotalIncomeForAllCompletedOrders() {
    List<Order> orders = new ArrayList<>();
    Customer customer = createCustomer("Oleg", "Brest");
    orders.add(createOrder(customer, OrderStatus.DELIVERED,
        createItem("Laptop", 1000, 1),
        createItem("Mouse", 50, 2)
    ));
    orders.add(createOrder(customer, OrderStatus.DELIVERED,
        createItem("Phone", 500, 1)
    ));
    orders.add(createOrder(customer, OrderStatus.PROCESSING,
        createItem("Tablet", 300, 1)
    ));
    double result = metricsService.getTotalIncomeForAllCompletedOrders(orders);
    assertEquals(1600, result, 0.001);
  }

  @Test
  @DisplayName("Should calculate average check only for delivered orders")
  void testGetAverageCheckForDeliveredOrders() {
    List<Order> orders = new ArrayList<>();
    Customer customer = createCustomer("Nikita", "Mogilev");

    orders.add(createOrder(customer, OrderStatus.DELIVERED,
        createItem("Laptop", 1000, 1)
    ));
    orders.add(createOrder(customer, OrderStatus.DELIVERED,
        createItem("Phone", 500, 1)
    ));
    orders.add(createOrder(customer, OrderStatus.SHIPPED,
        createItem("Tablet", 300, 1)
    ));
    double result = metricsService.getAverageCheckForDeliveredOrders(orders);
    assertEquals(750, result, 0.001);
  }

  @Test
  @DisplayName("Should find the most popular product by total quantity")
  void testGetTheMostPopularProduct() {
    List<Order> orders = new ArrayList<>();
    Customer customer = createCustomer("Nikita", "Vitebsk");

    orders.add(createOrder(customer, OrderStatus.DELIVERED,
        createItem("Laptop", 1000, 2),
        createItem("Phone", 50, 1)
    ));
    orders.add(createOrder(customer, OrderStatus.DELIVERED,
        createItem("Laptop", 1000, 1),
        createItem("Phone", 500, 3)
    ));
    orders.add(createOrder(customer, OrderStatus.DELIVERED,
        createItem("Phone", 500, 2)
    ));
    String result = metricsService.getTheMostPopularProduct(orders);
    assertEquals("Phone", result);
  }

  @Test
  @DisplayName("Should filter customers with 5 or more orders")
  void testGetCustomersWithFivePlusOrders() {
    List<Order> orders = new ArrayList<>();
    Customer customer1 = createCustomer("Kirill", "Minsk");
    Customer customer2 = createCustomer("Alice", "Gomel");
    for (int i = 0; i < 6; i++) {
      orders.add(createOrder(customer1, OrderStatus.DELIVERED, createItem("Laptop", 100, 1)));
    }
    for (int i = 0; i < 4; i++) {
      orders.add(createOrder(customer2, OrderStatus.DELIVERED, createItem("Phone", 100, 1)));
    }
    List<Customer> result = metricsService.getCustomersWithFivePlusOrders(orders);
    assertEquals(1, result.size());
    assertEquals("Kirill", result.get(0).getName());
  }

  @Test
  @DisplayName("Should handle empty orders list correctly")
  void testEmptyListCases() {
    List<Order> emptyOrders = new ArrayList<>();
    assertEquals(0, metricsService.getUniqueCities(emptyOrders).size());
    assertEquals(0, metricsService.getTotalIncomeForAllCompletedOrders(emptyOrders), 0.001);
    assertEquals(0, metricsService.getAverageCheckForDeliveredOrders(emptyOrders), 0.001);
    assertEquals("No products", metricsService.getTheMostPopularProduct(emptyOrders));
    assertEquals(0, metricsService.getCustomersWithFivePlusOrders(emptyOrders).size());
  }
}