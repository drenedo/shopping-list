package me.renedo.shopping.list.application.create;

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import me.renedo.shopping.item.application.create.ItemCreator;
import me.renedo.shopping.item.application.create.ItemCreator.CreateItemRequest;
import me.renedo.shopping.list.application.create.ShoppingListCreator.CreateShoppingListRequest;
import me.renedo.shopping.list.domain.ShoppingList;
import me.renedo.shopping.list.domain.ShoppingListRepository;
import me.renedo.shopping.status.domain.Status;

class ShoppingListCreatorShould {

    @Test
    public void save_a_valid_item(){
        ShoppingListRepository repository = mock(ShoppingListRepository.class);
        ItemCreator itemCreator = mock(ItemCreator.class);
        ShoppingListCreator creator = new ShoppingListCreator(repository, itemCreator);
        UUID item1Id = UUID.randomUUID();
        CreateItemRequest item1 = new CreateItemRequest(item1Id,"some-name", 10, "some-unit", null);
        UUID item2Id = UUID.randomUUID();
        CreateItemRequest item2 = new CreateItemRequest(item2Id,"some-name", 10, "some-unit", null);
        UUID id = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();
        CreateShoppingListRequest shoppingList = new CreateShoppingListRequest(id, now, "list-name", "list-description", List.of(item1, item2));

        creator.create(shoppingList);

        verify(itemCreator, atLeastOnce()).create(new CreateItemRequest(item1Id,"some-name", 10, "some-unit", id));
        verify(itemCreator, atLeastOnce()).create(new CreateItemRequest(item2Id,"some-name", 10, "some-unit", id));
        verify(repository, atLeastOnce()).save(new ShoppingList(id, now, "list-name", "list-description", null, Status.ACTIVE));
    }
}
