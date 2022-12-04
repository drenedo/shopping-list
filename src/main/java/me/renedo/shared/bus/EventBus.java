package me.renedo.shared.bus;

import java.util.List;

import me.renedo.shared.bus.event.DomainEvent;

public interface EventBus {

    void publish(List<DomainEvent<?>> events);
}
