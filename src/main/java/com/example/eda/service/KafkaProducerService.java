package com.example.eda.service;

import com.example.eda.schemas.EventSchema;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaProducerService {

    private static final String TOPIC = "event-stream";
    private final KafkaTemplate<String, EventSchema> kafkaTemplate;

    public void sendMessage(EventSchema event) {
        kafkaTemplate.send(new ProducerRecord<>(TOPIC, event.getEventId().toString(), event));
        log.info("Message published successfully");
    }

}
