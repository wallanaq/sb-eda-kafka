package com.example.eda.health;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.AdminClient;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class KafkaHealthIndicator implements HealthIndicator {

  private final KafkaAdmin kafkaAdmin;

  @Override
  public Health health() {
    try (AdminClient adminClient = AdminClient.create(kafkaAdmin.getConfigurationProperties())) {
      adminClient.describeCluster().nodes().get(3, TimeUnit.SECONDS);
      return Health.up().withDetail("Kafka", "Connection established").build();
    } catch (Exception e) {
      return Health.down().withDetail("kafka", "Connection failed").withException(e).build();
    }
  }

}
