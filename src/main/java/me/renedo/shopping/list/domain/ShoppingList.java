package me.renedo.shopping.list.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.apache.commons.lang3.builder.EqualsBuilder;

import me.renedo.shopping.item.domain.Item;
import me.renedo.shopping.status.domain.Status;

public class ShoppingList {

    private final UUID id;

    private final LocalDateTime dateTime;

    private final String name;

    private final String description;

    private final List<Item> items;

    private final Status status;


    public ShoppingList(ShoppingList list, List<Item> items) {
        id = list.getId();
        dateTime = list.getDateTime();
        name = list.getName();
        description = list.getDescription();
        this.items = items;
        status = list.getStatus();
    }

    public ShoppingList(UUID id, LocalDateTime dateTime, String name, String description, List<Item> items, Status status) {
        this.id = id;
        this.dateTime = dateTime;
        this.name = name;
        this.description = description;
        this.items = items;
        this.status = status;
    }


    public ShoppingList(UUID id, LocalDateTime dateTime, String name, String description) {
        this.id = id;
        this.dateTime = dateTime;
        this.name = name;
        this.description = description;
        this.status = Status.ACTIVE;
        this.items = null;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public UUID getId() {
        return id;
    }

    public List<Item> getItems() {
        return items;
    }

    public Status getStatus() {
        return status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;

        if (o == null || getClass() != o.getClass())
            return false;

        ShoppingList list = (ShoppingList) o;

        return new EqualsBuilder().append(id, list.id).append(dateTime, list.dateTime).append(name, list.name)
            .append(description, list.description).isEquals();
    }

    @Override public int hashCode() {
        return Objects.hash(id, name, dateTime, description, items);
    }
}
