package me.renedo.shopping.item.application.create;

import java.util.UUID;

import me.renedo.shared.Service;
import me.renedo.shared.bus.EventBus;
import me.renedo.shopping.item.domain.Item;
import me.renedo.shopping.item.domain.ItemRepository;
import me.renedo.shopping.status.domain.Status;

@Service
public class ItemCreator {

    private final ItemRepository itemRepository;

    private final EventBus eventBus;

    public ItemCreator(ItemRepository itemRepository, EventBus eventBus) {
        this.itemRepository = itemRepository;
        this.eventBus = eventBus;
    }

    public void create(CreateItemRequest request){
        Item item = new Item(request.id(), request.name(), request.amount(), request.unit(), request.brand, request.shoppingListId(), Status.ACTIVE);
        itemRepository.save(item);
        eventBus.publish(item.pullEvents());
    }

    public record CreateItemRequest(UUID id, String name, Integer amount, String unit, String brand, UUID shoppingListId ) {

    }
}
