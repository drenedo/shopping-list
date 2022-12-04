package me.renedo.shared.bus.event;

import java.util.ArrayList;
import java.util.List;

public class EventSource {
    private final List<DomainEvent<?>> events = new ArrayList<>();

    protected void record(DomainEvent<?> event){
        events.add(event);
    }

    public List<DomainEvent<?>> pullEvents(){
        List<DomainEvent<?>> currentEvents = events.stream().toList();
        events.clear();
        return currentEvents;
    }
}
