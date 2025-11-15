package com.actisys.orderservice.config;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

public abstract class IntegrationTestContainers {

  static PostgreSQLContainer<?> postgres;
  static KafkaContainer kafkaContainer;

  static {
    postgres = new PostgreSQLContainer<>("postgres:16")
        .withDatabaseName("order_service_db")
        .withUsername("test")
        .withPassword("test");
    postgres.start();

    kafkaContainer = new KafkaContainer(
        DockerImageName.parse("confluentinc/cp-kafka:7.5.0")
    );
    kafkaContainer.start();
  }

  @DynamicPropertySource
  static void setProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", postgres::getJdbcUrl);
    registry.add("spring.datasource.username", postgres::getUsername);
    registry.add("spring.datasource.password", postgres::getPassword);
    registry.add("spring.kafka.bootstrap-servers", kafkaContainer::getBootstrapServers);
  }
}
