package com.example.eda.service;

import com.example.eda.entity.EventEntity;
import com.example.eda.mapper.EventMapper;
import com.example.eda.repository.EventRepository;
import com.example.eda.schemas.EventSchema;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaConsumerService {

  private final EventMapper mapper;
  private final EventRepository repository;

  @KafkaListener(topics = "event-stream", groupId = "event-consumers")
  public void consume(ConsumerRecord<String, EventSchema> record) {
    EventSchema event = record.value();
    System.out.println(event);

    EventEntity entity = mapper.toEntity(event);

    this.repository.save(entity);

    log.info("Event saved successfully");
  }
}
