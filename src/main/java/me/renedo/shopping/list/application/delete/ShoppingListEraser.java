package me.renedo.shopping.list.application.delete;

import java.util.UUID;

import me.renedo.shopping.list.domain.ShoppingListRepository;
import me.renedo.shopping.shared.Service;

@Service
public class ShoppingListEraser {

    private final ShoppingListRepository shoppinglistRepository;

    public ShoppingListEraser(ShoppingListRepository shoppinglistRepository) {
        this.shoppinglistRepository = shoppinglistRepository;
    }

    public void delete(UUID id){
        //TODO verify list has not itemss
        shoppinglistRepository.delete(id);
    }
}
