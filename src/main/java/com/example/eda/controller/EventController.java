package com.example.eda.controller;

import com.example.eda.dto.EventDTO;
import com.example.eda.schemas.EventSchema;
import com.example.eda.service.KafkaProducerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/events")
public class EventController {

    private final KafkaProducerService service;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody EventDTO event) {

        var eventSchema = EventSchema.newBuilder()
            .setEventId(UUID.randomUUID().toString())
            .setName(event.name())
            .setDescription(event.description())
            .build();

        this.service.sendMessage(eventSchema);

        return ResponseEntity.accepted().build();
    }

}
