package me.renedo.shopping.item.domain;

import java.util.UUID;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import me.renedo.shared.bus.event.EventSource;
import me.renedo.shared.exception.NotAcceptableException;
import me.renedo.shopping.item.domain.event.ItemCreatedEvent;
import me.renedo.shopping.status.domain.Status;

public class Item extends EventSource {

    private final UUID id;

    private final String name;

    private final Integer amount;

    private final String unit;

    private final String brand;

    private final UUID listId;

    private final Status status;

    public Item(UUID id, String name, Integer amount, String unit, String brand, UUID listId, Status status) {
        if(name == null || name.isBlank()){
            throw new NotAcceptableException("Name is mandatory");
        }
        if(id == null){
            throw new NotAcceptableException("Id is mandatory");
        }
        if(amount == null || amount < 1){
            throw new NotAcceptableException("Amount is mandatory");
        }
        if(status == null){
            throw new NotAcceptableException("Status is mandatory");
        }
        if(listId == null){
            throw new NotAcceptableException("List is mandatory");
        }
        this.id = id;
        this.name = name.toLowerCase();
        this.amount = amount;
        this.unit = unit!=null ? unit.toLowerCase() : null;
        this.brand = brand!=null ? brand.toLowerCase() : null;
        this.listId = listId;
        this.status = status;
        this.record(new ItemCreatedEvent(id, name, unit, brand));
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getAmount() {
        return amount;
    }

    public String getBrand() {
        return brand;
    }

    public String getUnit() {
        return unit;
    }

    public Status getStatus() {
        return status;
    }

    public UUID getListId() {
        return listId;
    }

    @Override public boolean equals(Object o) {
        if (this == o)
            return true;

        if (o == null || getClass() != o.getClass())
            return false;

        Item item = (Item) o;

        return new EqualsBuilder().append(id, item.id).append(name, item.name).append(amount, item.amount).append(unit, item.unit)
            .append(brand, item.brand).append(listId, item.listId).isEquals();
    }

    @Override public int hashCode() {
        return new HashCodeBuilder(17, 37).append(id).append(name).append(amount).append(unit).append(brand).append(listId).toHashCode();
    }
}
