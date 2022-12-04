package me.renedo.shared.bus;

import java.util.List;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import me.renedo.shared.bus.event.DomainEvent;

@Component
public class MemoryEventBus implements EventBus{

    private final ApplicationEventPublisher applicationEventPublisher;

    public MemoryEventBus(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    public void publish(List<DomainEvent<?>> events) {
        events.forEach(applicationEventPublisher::publishEvent);
    }
}
