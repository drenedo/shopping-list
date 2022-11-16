package me.renedo.shopping.item.domain;

import java.util.UUID;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import me.renedo.shopping.status.domain.Status;

public class Item {

    private final UUID id;

    private final String name;

    private final Integer amount;

    private final String unit;

    private final UUID listId;

    private final Status status;

    public Item(UUID id, String name, Integer amount, String unit, UUID listId, Status status) {
        this.id = id;
        this.name = name;
        this.amount = amount;
        this.unit = unit;
        this.listId = listId;
        this.status = status;
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

        return new EqualsBuilder().append(id, item.id).append(name, item.name).append(amount, item.amount)
            .append(unit, item.unit).append(listId, item.listId).isEquals();
    }

    @Override public int hashCode() {
        return new HashCodeBuilder(17, 37).append(id).append(name).append(amount).append(unit).append(listId).toHashCode();
    }
}
