package com.actisys.paymentservice;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.utility.DockerImageName;

public abstract class IntegrationTestContainers {

  static MongoDBContainer mongoDBContainer;
  static KafkaContainer kafkaContainer;

  static {
    mongoDBContainer = new MongoDBContainer("mongo:6.0");
    mongoDBContainer.start();

    kafkaContainer = new KafkaContainer(
        DockerImageName.parse("confluentinc/cp-kafka:7.5.0")
    );
    kafkaContainer.start();
  }

  @DynamicPropertySource
  static void setProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    registry.add("spring.kafka.bootstrap-servers", kafkaContainer::getBootstrapServers);
    registry.add("random.api.url", () -> "http://localhost:8089");
  }
}
