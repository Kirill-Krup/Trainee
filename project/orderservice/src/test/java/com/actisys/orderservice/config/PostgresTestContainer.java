package com.actisys.orderservice.config;

import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;


public class PostgresTestContainer extends PostgreSQLContainer<PostgresTestContainer> {

  private static final String IMAGE_VERSION = "postgres:16";
  private static PostgresTestContainer container;

  private PostgresTestContainer() {
    super(DockerImageName.parse(IMAGE_VERSION));
    withDatabaseName("order_service_db");
    withUsername("test");
    withPassword("test");
  }

  public static synchronized PostgresTestContainer getInstance() {
    if (container == null) {
      container = new PostgresTestContainer();
    }
    return container;
  }

  @Override
  public void start() {
    super.start();
    System.setProperty("spring.datasource.url", getJdbcUrl());
    System.setProperty("spring.datasource.username", getUsername());
    System.setProperty("spring.datasource.password", getPassword());
  }

  @Override
  public void stop() {}
}
