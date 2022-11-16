package me.renedo.shopping.list.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ShoppingListRepository {

    ShoppingList save(ShoppingList list);

    List<ShoppingList> findAllPaginate(LocalDateTime time, int pageSize);

    Optional<ShoppingList> findById(UUID id);

    void delete(UUID id);
}
