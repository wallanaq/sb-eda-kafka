package com.example.eda.service;

import com.example.eda.schemas.EventSchema;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaConsumerService {

  private final KafkaTemplate<String, EventSchema> kafkaTemplate;

  @RetryableTopic(
    attempts = "3",
    dltTopicSuffix = "-dlt",
    backoff = @Backoff(delay = 3000)
  )
  @KafkaListener(
    topics = "event-stream",
    groupId = "event-consumers"
  )
  public void consume(ConsumerRecord<String, EventSchema> record) {
    EventSchema event = record.value();

    var correlationId = new String(record.headers().lastHeader("correlationId").value());

    try {

      if (event.getName().toString().contains("ERROR")) {
        log.error("Event error [eventId: {}][correlationId {}]", event.getEventId().toString(), correlationId);
        throw new RuntimeException("Simulated error");
      }

      log.info("Event saved successfully [eventId: {}][correlationId {}]", event.getEventId().toString(), correlationId);

    } catch (Exception e) {

      log.error("Failed to save event[eventId: {}][correlationId {}]", event.getEventId().toString(), correlationId);
      throw e;

    }
  }

  @DltHandler
  public void handleDltMessage(ConsumerRecord<String, EventSchema> record) {
    EventSchema event = record.value();
    var correlationId = new String(record.headers().lastHeader("correlationId").value());
    log.error("DLT received failed message [eventId: {}][correlationId {}]", event.getEventId().toString(), correlationId);
  }
}
