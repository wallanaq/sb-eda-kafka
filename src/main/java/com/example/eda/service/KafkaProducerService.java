package com.example.eda.service;

import com.example.eda.schemas.EventSchema;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.apache.kafka.common.header.internals.RecordHeaders;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaProducerService {

    private static final String TOPIC = "event-stream";
    private final KafkaTemplate<String, EventSchema> kafkaTemplate;

    public void sendMessage(EventSchema event, String correlationId) {

        var headers = new RecordHeaders();
        headers.add(new RecordHeader("correlationId", correlationId.getBytes(StandardCharsets.UTF_8)));

        var record = new ProducerRecord<>(TOPIC, null, event.getEventId().toString(), event, headers);

        kafkaTemplate.send(record);

        log.info("Message published successfully [eventId: {}][correlationId: {}]", event.getEventId().toString(), correlationId);
    }

}
