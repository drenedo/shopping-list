package me.renedo.shopping.list.application.create;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import me.renedo.shopping.item.application.create.ItemCreator;
import me.renedo.shopping.item.application.create.ItemCreator.CreateItemRequest;
import me.renedo.shopping.list.domain.ShoppingList;
import me.renedo.shopping.list.domain.ShoppingListRepository;
import me.renedo.shared.Service;
import me.renedo.shopping.status.domain.Status;

@Service
public class ShoppingListCreator {

    private final ShoppingListRepository shoppinglistRepository;

    private final ItemCreator itemCreator;


    public ShoppingListCreator(ShoppingListRepository shoppinglistRepository, ItemCreator itemCreator) {
        this.shoppinglistRepository = shoppinglistRepository;
        this.itemCreator = itemCreator;
    }

    public void create(CreateShoppingListRequest request) {
        shoppinglistRepository.save(new ShoppingList(request.id(), request.dateTime(), request.name(), request.description(), null, Status.ACTIVE));
        request.items.stream()
            .map(r->new CreateItemRequest(r.id(), r.name(), r.amount(), r.unit(), r.brand(), request.id()))
            .forEach(itemCreator::create);
    }


    public record CreateShoppingListRequest(UUID id, LocalDateTime dateTime, String name, String description,
                                            List<CreateItemRequest> items) {

    }
}
