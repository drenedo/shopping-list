package me.renedo.shopping.item.application.create;

import java.util.UUID;

import me.renedo.shopping.item.domain.Item;
import me.renedo.shopping.item.domain.ItemRepository;
import me.renedo.shopping.shared.Service;
import me.renedo.shopping.status.domain.Status;

@Service
public class ItemCreator {

    private final ItemRepository itemRepository;

    public ItemCreator(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public void create(CreateItemRequest request){
        itemRepository.save(new Item(request.id(), request.name(), request.amount(), request.unit(), request.shoppingListId(), Status.ACTIVE));
    }

    public record CreateItemRequest(UUID id, String name, Integer amount, String unit, UUID shoppingListId ) {

    }
}
