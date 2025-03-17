package com.example.eda.controller;

import com.example.eda.dto.EventDTO;
import com.example.eda.schemas.EventSchema;
import com.example.eda.schemas.HeaderSchema;
import com.example.eda.service.KafkaProducerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/events")
public class EventController {

  private final KafkaProducerService service;

  @PostMapping
  public ResponseEntity<?> create(@RequestBody EventDTO event, @RequestHeader HttpHeaders headers) {

    var headerSchema = HeaderSchema.newBuilder()
      .setEventId(UUID.randomUUID().toString())
      .setSource(headers.getFirst("client-id"))
      .setCorrelationId(headers.getFirst("correlation-id"))
      .setTimestamp(Instant.now())
      .build();

    var eventSchema = EventSchema.newBuilder()
      .setName(event.name())
      .setDescription(event.description())
      .build();

    this.service.sendMessage(eventSchema, headerSchema);

    return ResponseEntity.accepted().build();
  }

}
