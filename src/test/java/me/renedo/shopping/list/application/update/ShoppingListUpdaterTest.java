package me.renedo.shopping.list.application.update;

import static me.renedo.shopping.status.domain.Status.ACTIVE;
import static me.renedo.shopping.status.domain.Status.INACTIVE;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.atMostOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import me.renedo.shopping.item.application.retrieve.ItemRetriever;
import me.renedo.shopping.item.application.update.ItemUpdater;
import me.renedo.shopping.item.domain.Item;
import me.renedo.shopping.list.domain.ShoppingListRepository;

class ShoppingListUpdaterTest {

    private final ShoppingListRepository repository = mock(ShoppingListRepository.class);
    private final ItemRetriever itemRetriever = mock(ItemRetriever.class);
    private final ItemUpdater itemUpdater = mock(ItemUpdater.class);
    private final ShoppingListUpdater updater = new ShoppingListUpdater(repository, itemUpdater, itemRetriever);

    @Test
    void update_list_when_all_its_items_are_not_active() {
        UUID uuid = UUID.randomUUID();
        when(itemRetriever.retrieveListItems(uuid)).thenReturn(
            List.of(new Item(UUID.randomUUID(), "some-item", 1, "some-unit", "some-brand", uuid, INACTIVE)));

        updater.checkListForUpdates(uuid);

        verify(repository, atMostOnce()).updateStatus(uuid, INACTIVE);
        verifyNoInteractions(itemUpdater);
    }

    @Test
    void update_list_status_has_active_items() {
        UUID uuid = UUID.randomUUID();
        List<Item> items = List.of(new Item(UUID.randomUUID(), "some-item", 1, "some-unit", "some-brand", uuid, ACTIVE));
        when(itemRetriever.retrieveListItems(uuid)).thenReturn(items);

        updater.updateStatus(uuid, INACTIVE);

        verify(repository, atMostOnce()).updateStatus(uuid, INACTIVE);
        verify(itemUpdater, atLeastOnce()).updateStatus(items, INACTIVE);
    }

    @Test
    void update_list_status_has_no_items_active() {
        UUID uuid = UUID.randomUUID();
        List<Item> items = List.of(new Item(UUID.randomUUID(), "some-item", 1, "some-unit", "some-brand", uuid, INACTIVE));
        when(itemRetriever.retrieveListItems(uuid)).thenReturn(items);

        updater.updateStatus(uuid, INACTIVE);

        verify(repository, atMostOnce()).updateStatus(uuid, INACTIVE);
        verifyNoInteractions(itemUpdater);
    }

    @Test
    void dont_update_list_when_at_east_one_item_is_active() {
        UUID uuid = UUID.randomUUID();
        when(itemRetriever.retrieveListItems(uuid)).thenReturn(
            List.of(new Item(UUID.randomUUID(), "some-item", 1, "some-unit", "some-brand", uuid, ACTIVE)));

        updater.checkListForUpdates(uuid);

        verifyNoInteractions(repository);
    }
}
