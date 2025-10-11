package com.actisys.orderservice;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.configureFor;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.actisys.orderservice.dto.OrderDTO;
import com.actisys.orderservice.dto.UserDTO;
import com.actisys.orderservice.exception.OrderNotFoundException;
import com.actisys.orderservice.model.enumClasses.StatusType;
import com.actisys.orderservice.repository.OrderRepository;
import com.actisys.orderservice.service.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@Testcontainers
@Transactional
@Rollback
public class OrderServiceIntegrationTest {

  @Container
  static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16")
      .withDatabaseName("order_service_db")
      .withUsername("test")
      .withPassword("test");

  @DynamicPropertySource
  static void configurateProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", postgres::getJdbcUrl);
    registry.add("spring.datasource.username", postgres::getUsername);
    registry.add("spring.datasource.password", postgres::getPassword);
  }

  private static WireMockServer wireMockServer;
  private static final ObjectMapper mapper = new ObjectMapper();

  @Autowired
  private OrderService orderService;

  @Autowired
  private OrderRepository orderRepository;

  private UserDTO testUser;

  @BeforeAll
  static void startWireMock() {
    wireMockServer = new WireMockServer(9561);
    wireMockServer.start();
    configureFor("localhost", 9561);
  }

  @AfterAll
  static void stopWireMock() {
    wireMockServer.stop();
  }

  @BeforeEach
  void setup() throws Exception {
    testUser = new UserDTO(
        1L,
        "Kirill",
        "Krupenin",
        Timestamp.valueOf("2005-12-19 06:30:27"),
        "kiryl.krupenin@innowise.com"
    );

    wireMockServer.stubFor(get(urlEqualTo("/users/1"))
        .willReturn(aResponse()
            .withHeader("Content-Type", "application/json")
            .withBody(mapper.writeValueAsString(testUser))
            .withStatus(200)));
  }

  @Test
  @DisplayName("This test should create order")
  void createOrder() {
    OrderDTO created = orderService.createOrder(
        new OrderDTO(null, testUser, StatusType.PENDING, LocalDateTime.now())
    );

    assertThat(created.getId()).isNotNull();
    assertThat(orderRepository.findById(created.getId())).isPresent();
    assertThat(created.getStatus()).isEqualTo(StatusType.PENDING);
  }

  @Test
  @DisplayName("This test should find order by id")
  void shouldFindOrderById() {
    OrderDTO created = orderService.createOrder(
        new OrderDTO(null, testUser, StatusType.CONFIRMED, LocalDateTime.now())
    );

    OrderDTO found = orderService.getOrderById(created.getId()).orElseThrow();

    assertThat(found.getId()).isEqualTo(created.getId());
    assertThat(found.getStatus()).isEqualTo(StatusType.CONFIRMED);
  }

  @Test
  @DisplayName("This test should update order")
  void updateOrder() {
    OrderDTO created = orderService.createOrder(
        new OrderDTO(null, testUser, StatusType.PROCESSING, LocalDateTime.now())
    );

    OrderDTO updated = orderService.updateOrder(
        created.getId(),
        new OrderDTO(created.getId(), testUser, StatusType.SHIPPED, created.getCreationDate())
    );

    assertThat(updated.getStatus()).isEqualTo(StatusType.SHIPPED);
  }

  @Test
  @DisplayName("This test should delete order")
  void deleteOrder() {
    OrderDTO created = orderService.createOrder(
        new OrderDTO(null, testUser, StatusType.PENDING, LocalDateTime.now())
    );

    orderService.deleteOrder(created.getId());
    assertThat(orderRepository.findById(created.getId())).isEmpty();
  }

  @Test
  @DisplayName("This test should 'OrderNotFoundException'")
  void orderNotFound() {
    assertThrows(
        OrderNotFoundException.class,
        () -> orderService.getOrderById(999L).orElseThrow()
    );
  }

  @Test
  @DisplayName("This test should find all orders by statuses")
  void findOrdersByStatuses() {
    orderService.createOrder(new OrderDTO(null, testUser, StatusType.SHIPPED, LocalDateTime.now()));
    orderService.createOrder(
        new OrderDTO(null, testUser, StatusType.DELIVERED, LocalDateTime.now()));
    orderService.createOrder(
        new OrderDTO(null, testUser, StatusType.CANCELLED, LocalDateTime.now()));

    List<OrderDTO> found = orderService.getOrdersByStatusIn(
        List.of(StatusType.SHIPPED, StatusType.DELIVERED));

    assertThat(found).hasSize(2);
    assertThat(found)
        .allMatch(
            o -> o.getStatus() == StatusType.SHIPPED || o.getStatus() == StatusType.DELIVERED);
  }
}
