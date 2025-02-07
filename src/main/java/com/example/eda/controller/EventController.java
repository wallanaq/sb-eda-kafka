package com.example.eda.controller;

import com.example.eda.dto.EventDTO;
import com.example.eda.schemas.EventSchema;
import com.example.eda.service.KafkaProducerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/events")
public class EventController {

    private final KafkaProducerService service;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody EventDTO event, @RequestHeader(value = "correlationId", required = true) String correlationId) {

        var eventSchema = EventSchema.newBuilder()
            .setEventId(UUID.randomUUID().toString())
            .setName(event.name())
            .setDescription(event.description())
            .build();

        this.service.sendMessage(eventSchema, correlationId);

        return ResponseEntity.accepted().build();
    }

}
