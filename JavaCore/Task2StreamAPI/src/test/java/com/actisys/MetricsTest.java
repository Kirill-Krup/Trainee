package com.actisys;
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
    void testGetUniqueCities() {
        List<Order> orders = new ArrayList<>();
        Customer customer1 = createCustomer("Кирилл", "Минск");
        Customer customer2 = createCustomer("Алиса", "Минск");
        Customer customer3 = createCustomer("Анна", "Гомель");
        orders.add(createOrder(customer1, OrderStatus.DELIVERED, createItem("Ноутбук", 1000, 1)));
        orders.add(createOrder(customer2, OrderStatus.DELIVERED, createItem("Телефон", 500, 1)));
        orders.add(createOrder(customer3, OrderStatus.PROCESSING, createItem("Планшет", 300, 1)));
        List<String> result = metricsService.getUniqueCities(orders);
        assertEquals(1, result.size());
        assertTrue(result.contains("Гомель"));
    }

    @Test
    void testGetTotalIncomeForAllCompletedOrders() {
        List<Order> orders = new ArrayList<>();
        Customer customer = createCustomer("Олег", "Брест");
        orders.add(createOrder(customer, OrderStatus.DELIVERED,
                createItem("Ноутбук", 1000, 1),
                createItem("Мышка", 50, 2)
        ));
        orders.add(createOrder(customer, OrderStatus.DELIVERED,
                createItem("Телефон", 500, 1)
        ));
        orders.add(createOrder(customer, OrderStatus.PROCESSING,
                createItem("Планшет", 300, 1)
        ));
        double result = metricsService.getTotalIncomeForAllCompletedOrders(orders);
        assertEquals(1600, result, 0.001);
    }

    @Test
    void testGetAverageCheckForDeliveredOrders() {
        List<Order> orders = new ArrayList<>();
        Customer customer = createCustomer("Никита", "Могилёв");

        orders.add(createOrder(customer, OrderStatus.DELIVERED,
                createItem("Ноутбук", 1000, 1)
        ));
        orders.add(createOrder(customer, OrderStatus.DELIVERED,
                createItem("Телефон", 500, 1)
        ));
        orders.add(createOrder(customer, OrderStatus.SHIPPED,
                createItem("Планшет", 300, 1)
        ));
        double result = metricsService.getAverageCheckForDeliveredOrders(orders);
        assertEquals(750, result, 0.001);
    }

    @Test
    void testGetTheMostPopularProduct() {
        List<Order> orders = new ArrayList<>();
        Customer customer = createCustomer("Никита", "Витебск");

        orders.add(createOrder(customer, OrderStatus.DELIVERED,
                createItem("Ноутбук", 1000, 2),
                createItem("Телефон", 50, 1)
        ));
        orders.add(createOrder(customer, OrderStatus.DELIVERED,
                createItem("Ноутбук", 1000, 1),
                createItem("Телефон", 500, 3)
        ));
        orders.add(createOrder(customer, OrderStatus.DELIVERED,
                createItem("Телефон", 500, 2)
        ));
        String result = metricsService.getTheMostPopularProduct(orders);
        assertEquals("Телефон", result);
    }

    @Test
    void testGetCustomersWithFivePlusOrders() {
        List<Order> orders = new ArrayList<>();
        Customer customer1 = createCustomer("Кирилл", "Минск");
        Customer customer2 = createCustomer("Алиса", "Гомель");
        for (int i = 0; i < 6; i++) {
            orders.add(createOrder(customer1, OrderStatus.DELIVERED, createItem("Ноутбук", 100, 1)));
        }
        for (int i = 0; i < 4; i++) {
            orders.add(createOrder(customer2, OrderStatus.DELIVERED, createItem("Телефон", 100, 1)));
        }
        List<Customer> result = metricsService.getCustomersWithFivePlusOrders(orders);
        assertEquals(1, result.size());
        assertEquals("Кирилл", result.get(0).getName());
    }

    @Test
    void testEmptyListCases() {
        List<Order> emptyOrders = new ArrayList<>();
        assertEquals(0, metricsService.getUniqueCities(emptyOrders).size());
        assertEquals(0, metricsService.getTotalIncomeForAllCompletedOrders(emptyOrders), 0.001);
        assertEquals(0, metricsService.getAverageCheckForDeliveredOrders(emptyOrders), 0.001);
        assertEquals("No products", metricsService.getTheMostPopularProduct(emptyOrders));
        assertEquals(0, metricsService.getCustomersWithFivePlusOrders(emptyOrders).size());
    }
}