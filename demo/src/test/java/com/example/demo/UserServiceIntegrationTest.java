package com.example.demo;

import com.example.demo.dto.CardInfoDTO;
import com.example.demo.dto.UserDTO;
import com.example.demo.service.UserService;
import java.sql.Timestamp;
import java.util.List;
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

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Testcontainers
@Transactional
@Rollback
class UserServiceIntegrationTest {

  @Container
  static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16")
      .withDatabaseName("testdb")
      .withUsername("test")
      .withPassword("test");

  @DynamicPropertySource
  static void configureProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", postgres::getJdbcUrl);
    registry.add("spring.datasource.username", postgres::getUsername);
    registry.add("spring.datasource.password", postgres::getPassword);
  }

  @Autowired
  private UserService userService;

  private UserDTO testUser;

  @BeforeEach
  void setup() {
    CardInfoDTO card = new CardInfoDTO(
        null,
        "1234567890123456",
        "Alice Smith",
        new Timestamp(System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 365)
    );

    testUser = new UserDTO(
        null,
        "Alice",
        "Smith",
        new Timestamp(System.currentTimeMillis()),
        "alice@example.com",
        List.of(card)
    );
  }

  @Test
  @DisplayName("Create user and get him by id and email")
  void testCreateAndGetUser() {
    UserDTO created = userService.createUser(testUser);
    assertThat(created.getId()).isNotNull();
    assertThat(created.getCards()).hasSize(1);

    UserDTO fetched = userService.getUserById(created.getId()).orElseThrow();
    assertThat(fetched.getName()).isEqualTo("Alice");
    assertThat(fetched.getCards()).hasSize(1);

    UserDTO fetchedByEmail = userService.getUserByEmail("alice@example.com");
    assertThat(fetchedByEmail.getId()).isEqualTo(created.getId());
  }

  @Test
  @DisplayName("Update user test")
  void testUpdateUser() {
    UserDTO created = userService.createUser(testUser);
    UserDTO updatedDTO = new UserDTO(
        created.getId(),
        "AliceUpdated",
        "SmithUpdated",
        created.getBirthDate(),
        "alice.updated@example.com",
        created.getCards()
    );

    UserDTO updated = userService.updateUser(created.getId(), updatedDTO);
    assertThat(updated.getName()).isEqualTo("AliceUpdated");
    assertThat(updated.getEmail()).isEqualTo("alice.updated@example.com");
  }

  @Test
  @DisplayName("Delete user test")
  void testDeleteUser() {
    UserDTO created = userService.createUser(testUser);
    Long id = created.getId();

    UserDTO deleted = userService.deleteUser(id);
    assertThat(deleted.getId()).isEqualTo(id);
    assertThat(userService.getUserById(id)).isEmpty();
  }

  @Test
  @DisplayName("Get users by ids")
  void testGetUsersByIds() {
    UserDTO user1 = userService.createUser(testUser);
    UserDTO user2DTO = new UserDTO(
        null,
        "Bob",
        "Marley",
        new Timestamp(System.currentTimeMillis()),
        "bob@example.com",
        List.of()
    );
    UserDTO user2 = userService.createUser(user2DTO);
    List<UserDTO> users = userService.getUsersByIds(List.of(user1.getId(), user2.getId()));
    assertThat(users).hasSize(2);
    assertThat(users).extracting(UserDTO::getEmail)
        .containsExactlyInAnyOrder("alice@example.com", "bob@example.com");
  }

  @Test
  @DisplayName("Create user without cards")
  void testCreateUserWithoutCards() {
    UserDTO userWithoutCards = new UserDTO(
        null,
        "Charlie",
        "Brown",
        new Timestamp(System.currentTimeMillis()),
        "charlie@example.com",
        null
    );
    UserDTO created = userService.createUser(userWithoutCards);
    assertThat(created.getId()).isNotNull();
    assertThat(created.getCards()).isNullOrEmpty();
  }
}