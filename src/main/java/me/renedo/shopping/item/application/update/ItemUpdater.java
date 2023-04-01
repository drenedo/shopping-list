package me.renedo.shopping.item.application.update;

import java.util.List;
import java.util.UUID;

import org.springframework.context.annotation.Lazy;

import me.renedo.shared.exception.NotFoundException;
import me.renedo.shopping.item.domain.Item;
import me.renedo.shopping.item.domain.ItemRepository;
import me.renedo.shopping.list.application.update.ShoppingListUpdater;
import me.renedo.shared.Service;
import me.renedo.shopping.status.domain.Status;

@Service
public class ItemUpdater {

    private final ItemRepository repository;

    private final ShoppingListUpdater shoppingListUpdater;

    public ItemUpdater(ItemRepository repository, @Lazy ShoppingListUpdater shoppingListUpdater) {
        this.repository = repository;
        this.shoppingListUpdater = shoppingListUpdater;
    }

    public void updateStatus(UUID id, Status status) {
        Item item = repository.findById(id).orElseThrow(() -> new NotFoundException("Item not exits"));
        repository.updateStatus(id, status);
        shoppingListUpdater.checkListForUpdates(item.getListId());
    }

    public void updateStatus(List<Item> items, Status status) {
        repository.updateStatus(items, status);
    }
}
