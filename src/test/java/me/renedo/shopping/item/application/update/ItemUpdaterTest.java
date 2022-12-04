package me.renedo.shopping.item.application.update;

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import me.renedo.shopping.item.domain.Item;
import me.renedo.shopping.item.domain.ItemRepository;
import me.renedo.shopping.list.application.update.ShoppingListUpdater;
import me.renedo.shopping.status.domain.Status;

class ItemUpdaterTest {

    @Test
    void update_status_of_item(){
        ItemRepository repository = mock(ItemRepository.class);
        ShoppingListUpdater shoppingListUpdater = mock(ShoppingListUpdater.class);
        ItemUpdater updater = new ItemUpdater(repository, shoppingListUpdater);
        UUID id = UUID.randomUUID();
        UUID listId = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.of(new Item(id, "some-name", 2,"some-unir", "some-brand", listId, Status.ACTIVE)));

        updater.updateStatus(id, Status.INACTIVE);

        verify(repository, atLeastOnce()).updateStatus(id, Status.INACTIVE);
        verify(shoppingListUpdater, atLeastOnce()).checkListForUpdates(listId);
    }
}
