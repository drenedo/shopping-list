package me.renedo.shopping.list.application.update;

import static me.renedo.shopping.status.domain.Status.ACTIVE;

import java.util.List;
import java.util.UUID;

import me.renedo.shopping.item.application.retrieve.ItemRetriever;
import me.renedo.shopping.item.application.update.ItemUpdater;
import me.renedo.shopping.item.domain.Item;
import me.renedo.shopping.list.domain.ShoppingListRepository;
import me.renedo.shared.Service;
import me.renedo.shopping.status.domain.Status;

@Service
public class ShoppingListUpdater {

    private final ShoppingListRepository shoppingListRepository;

    private final ItemUpdater itemUpdater;

    private final ItemRetriever itemRetriever;

    public ShoppingListUpdater(ShoppingListRepository shoppingListRepository, ItemUpdater itemUpdater, ItemRetriever itemRetriever) {
        this.shoppingListRepository = shoppingListRepository;
        this.itemUpdater = itemUpdater;
        this.itemRetriever = itemRetriever;
    }

    public void checkListForUpdates(UUID id){
        List<Item> items = itemRetriever.retrieveListItems(id);
        if(items.size() == items.stream().filter(item -> !item.getStatus().equals(ACTIVE)).count()){
            updateStatus(id, Status.INACTIVE);
        }
    }

    public void updateStatus(UUID id, Status status){
        shoppingListRepository.updateStatus(id, status);
        if(status==Status.INACTIVE){
            List<Item> items = itemRetriever.retrieveListItems(id).stream().filter(i -> i.getStatus().equals(ACTIVE)).toList();
            if(items.size() > 0) {
                itemUpdater.updateStatus(items, Status.INACTIVE);
            }
        }
    }
}
