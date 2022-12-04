package me.renedo.shopping.item.domain;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import me.renedo.shopping.status.domain.Status;

public interface ItemRepository {

    Item save(Item item);

    Optional<Item> findById(UUID id);

    void delete(UUID id);

    List<Item> findInList(UUID id);

    void updateStatus(UUID id, Status status);

    void updateStatus(List<Item> items, Status status);
}
