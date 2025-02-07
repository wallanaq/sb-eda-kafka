package com.example.eda.service;

import com.example.eda.entity.EventEntity;
import com.example.eda.mapper.EventMapper;
import com.example.eda.repository.EventRepository;
import com.example.eda.schemas.EventSchema;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaConsumerService {

  private final EventMapper mapper;
  private final EventRepository repository;
  private final KafkaTemplate<String, EventSchema> kafkaTemplate;

  private static final String TOPIC_DLT = "event-stream-dlt";

  @KafkaListener(topics = "event-stream", groupId = "event-consumers")
  public void consume(ConsumerRecord<String, EventSchema> record) {
    EventSchema event = record.value();

    EventEntity entity = mapper.toEntity(event);

    var correlationId = new String(record.headers().lastHeader("correlationId").value());

    try {

      if (event.getName().toString().contains("ERROR")) {
          log.error("Database error [eventId: {}][correlationId {}]", event.getEventId().toString(), correlationId);
        throw new RuntimeException("Simulated database error");
      }

      this.repository.save(entity);
      log.info("Event saved successfully [eventId: {}][correlationId {}]", event.getEventId().toString(), correlationId);

    } catch (Exception e) {

      log.error("Failed to save event[eventId: {}][correlationId {}]", event.getEventId().toString(), correlationId);
      kafkaTemplate.send(TOPIC_DLT, event.getEventId().toString(), event);

    }
  }
}
