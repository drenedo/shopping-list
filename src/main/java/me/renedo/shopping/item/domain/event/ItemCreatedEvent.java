package me.renedo.shopping.item.domain.event;

import java.util.UUID;

import me.renedo.shared.bus.event.DomainEvent;

public class ItemCreatedEvent extends DomainEvent<ItemCreatedEvent> {

    private final String name;

    private final String unit;

    private final String brand;

    public ItemCreatedEvent(UUID domainId, String name, String unit, String brand) {
        super(domainId, UUID.randomUUID());
        this.name = name;
        this.unit = unit;
        this.brand = brand;
    }

    public String getName() {
        return name;
    }

    public String getUnit() {
        return unit;
    }

    public String getBrand() {
        return brand;
    }

    @Override
    public String toString() {
        return "ItemCreatedEvent{" +
            "name='" + name + '\'' +
            "} " + super.toString();
    }
}
