package com.eventostec.api.domain.event;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity()
@Table(name = "event")
public class Event {
    @Id
    @GeneratedValue
    private UUID id;
}
