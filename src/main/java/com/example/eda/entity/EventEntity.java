package com.example.eda.entity;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "events")
@NoArgsConstructor
public class EventEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private String Id;
  private String eventId;
  private String name;
  private String description;

}
