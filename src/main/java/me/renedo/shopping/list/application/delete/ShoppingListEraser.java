package me.renedo.shopping.list.application.delete;

import java.util.UUID;

import me.renedo.shopping.item.application.delete.ItemEraser;
import me.renedo.shopping.item.application.retrieve.ItemRetriever;
import me.renedo.shopping.list.domain.ShoppingListRepository;
import me.renedo.shopping.shared.Service;

@Service
public class ShoppingListEraser {

    private final ShoppingListRepository shoppinglistRepository;

    private final ItemRetriever itemRetriever;

    private final ItemEraser itemEraser;

    public ShoppingListEraser(ShoppingListRepository shoppinglistRepository, ItemRetriever itemRetriever, ItemEraser itemEraser) {
        this.shoppinglistRepository = shoppinglistRepository;
        this.itemRetriever = itemRetriever;
        this.itemEraser = itemEraser;
    }

    public void delete(UUID id){
        itemRetriever.retrieveListItems(id).forEach(ie -> itemEraser.delete(ie.getId()));
        shoppinglistRepository.delete(id);
    }
}
