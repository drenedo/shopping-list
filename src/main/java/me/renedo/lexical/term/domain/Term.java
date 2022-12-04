package me.renedo.lexical.term.domain;

import java.time.LocalDateTime;
import java.util.UUID;

import me.renedo.lexical.type.domain.Type;

public class Term {
    private final UUID id;

    private final String name;

    private final Integer times;

    private final Type type;

    private final LocalDateTime updated;

    public Term(UUID id, String name, Integer times, Type type, LocalDateTime updated) {
        this.id = id;
        this.name = name;
        this.times = times;
        this.type = type;
        this.updated = updated;
    }

    public Term(String name, Type type) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.type = type;
        this.times = 1;
        this.updated = LocalDateTime.now();
    }

    public Term(String name, Type type, Integer times) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.type = type;
        this.times = times;
        this.updated = LocalDateTime.now();
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getTimes() {
        return times;
    }

    public LocalDateTime getUpdated() {
        return updated;
    }

    public Type getType() {
        return type;
    }
}
