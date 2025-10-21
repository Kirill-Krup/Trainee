package com.actisys.orderservice;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.actisys.orderservice.dto.OrderDTO;
import com.actisys.orderservice.exception.OrderNotFoundException;
import com.actisys.orderservice.model.enumClasses.StatusType;
import com.actisys.orderservice.repository.OrderRepository;
import com.actisys.orderservice.service.OrderService;
import com.actisys.orderservice.config.PostgresTestContainer;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.common.ConsoleNotifier;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
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
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@Testcontainers
@Transactional
@Rollback
public class OrderServiceIntegrationTest {

  @Container
  static final PostgresTestContainer postgres = PostgresTestContainer.getInstance();

  @DynamicPropertySource
  static void configurateProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", postgres::getJdbcUrl);
    registry.add("spring.datasource.username", postgres::getUsername);
    registry.add("spring.datasource.password", postgres::getPassword);
    registry.add("user.service.url", () -> "http://localhost:9561");
  }

  private static WireMockServer wireMockServer;

  @Autowired
  private OrderService orderService;

  @Autowired
  private OrderRepository orderRepository;

  private final String testEmail = "kiryl.krupenin@innowise.com";
  private final Long testUserId = 1L;

  @BeforeAll
  static void startWireMock() {
    wireMockServer = new WireMockServer(
        WireMockConfiguration.options()
            .port(9561)
            .notifier(new ConsoleNotifier(false))
    );
    wireMockServer.start();
    configureFor("localhost", 9561);
  }

  @AfterAll
  static void stopWireMock() {
    if (wireMockServer != null) {
      wireMockServer.stop();
    }
  }

  @BeforeEach
  void setup() {
    wireMockServer.resetAll();

    wireMockServer.stubFor(get(urlMatching("/api/v1/users/get-id-by-email/.*"))
        .willReturn(aResponse()
            .withStatus(200)
            .withHeader("Content-Type", "application/json")
            .withBody("1")));
  }

  @Test
  @DisplayName("Should create order successfully")
  void createOrder() {
    OrderDTO created = orderService.createOrder(
        new OrderDTO(null, null, StatusType.PENDING, LocalDateTime.now(), null),
        testEmail
    );

    verify(getRequestedFor(urlMatching("/api/v1/users/get-id-by-email/.*kiryl\\.krupenin.*")));

    assertThat(created.getId()).isNotNull();
    assertThat(orderRepository.findById(created.getId())).isPresent();
    assertThat(created.getStatus()).isEqualTo(StatusType.PENDING);
    assertThat(created.getUserId()).isEqualTo(testUserId);
  }

  @Test
  @DisplayName("Should find order by id")
  void shouldFindOrderById() {
    OrderDTO created = orderService.createOrder(
        new OrderDTO(null, null, StatusType.CONFIRMED, LocalDateTime.now(), null),
        testEmail
    );

    OrderDTO found = orderService.getOrderById(created.getId()).orElseThrow();

    assertThat(found.getId()).isEqualTo(created.getId());
    assertThat(found.getStatus()).isEqualTo(StatusType.CONFIRMED);
    assertThat(found.getUserId()).isEqualTo(testUserId);
  }

  @Test
  @DisplayName("Should throw OrderNotFoundException when order not found")
  void orderNotFound() {
    org.junit.jupiter.api.Assertions.assertThrows(
        OrderNotFoundException.class,
        () -> orderService.getOrderById(999L).orElseThrow()
    );
  }

  @Test
  @DisplayName("Should find all orders by statuses")
  void findOrdersByStatuses() {
    orderService.createOrder(new OrderDTO(null, null, StatusType.SHIPPED, LocalDateTime.now(), null), testEmail);
    orderService.createOrder(new OrderDTO(null, null, StatusType.DELIVERED, LocalDateTime.now(), null), testEmail);
    orderService.createOrder(new OrderDTO(null, null, StatusType.CANCELLED, LocalDateTime.now(), null), testEmail);

    List<OrderDTO> found = orderService.getOrdersByStatusIn(List.of(StatusType.SHIPPED, StatusType.DELIVERED));

    assertThat(found).hasSize(2);
    assertThat(found).allMatch(
        o -> o.getStatus() == StatusType.SHIPPED || o.getStatus() == StatusType.DELIVERED);
    assertThat(found).allMatch(o -> o.getUserId().equals(testUserId));
  }

  @Test
  @DisplayName("Should find orders by ids")
  void findOrdersByIds() {
    OrderDTO order1 = orderService.createOrder(
        new OrderDTO(null, null, StatusType.PENDING, LocalDateTime.now(), null),
        testEmail
    );
    OrderDTO order2 = orderService.createOrder(
        new OrderDTO(null, null, StatusType.CONFIRMED, LocalDateTime.now(), null),
        testEmail
    );

    List<OrderDTO> found = orderService.getOrdersByIdIn(List.of(order1.getId(), order2.getId()));

    assertThat(found).hasSize(2);
    assertThat(found).extracting(OrderDTO::getId).containsExactlyInAnyOrder(order1.getId(), order2.getId());
  }
}
