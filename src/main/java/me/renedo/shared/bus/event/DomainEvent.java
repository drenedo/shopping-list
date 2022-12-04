package me.renedo.shared.bus.event;

import java.util.UUID;

public abstract class DomainEvent<T extends DomainEvent<?>> {
    private final UUID domainId;
    private final UUID eventId;

    protected DomainEvent(UUID domainId, UUID eventId) {
        this.domainId = domainId;
        this.eventId = eventId;
    }

    public UUID getDomainId() {
        return domainId;
    }

    public UUID getEventId() {
        return eventId;
    }
}
