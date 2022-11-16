package me.renedo.shopping.list.application.delete;

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.atMostOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import me.renedo.shopping.item.application.delete.ItemEraser;
import me.renedo.shopping.item.application.retrieve.ItemRetriever;
import me.renedo.shopping.item.domain.Item;
import me.renedo.shopping.list.domain.ShoppingListRepository;
import me.renedo.shopping.status.domain.Status;

class ShoppingListEraserTest {


    @Test
    void delete_list() {
        ShoppingListRepository repository = mock(ShoppingListRepository.class);
        ItemRetriever itemRetriever = mock(ItemRetriever.class);
        ItemEraser itemEraser = mock(ItemEraser.class);
        ShoppingListEraser eraser = new ShoppingListEraser(repository, itemRetriever, itemEraser);
        UUID id = UUID.randomUUID();

        eraser.delete(id);

        verify(itemRetriever, atLeastOnce()).retrieveListItems(id);
        verify(repository, atLeastOnce()).delete(id);
    }

    @Test
    void delete_list_with_items() {
        ShoppingListRepository repository = mock(ShoppingListRepository.class);
        ItemRetriever itemRetriever = mock(ItemRetriever.class);
        ItemEraser itemEraser = mock(ItemEraser.class);
        ShoppingListEraser eraser = new ShoppingListEraser(repository, itemRetriever, itemEraser);
        UUID id = UUID.randomUUID();
        when(itemRetriever.retrieveListItems(id)).thenReturn(List.of(new Item(UUID.randomUUID(), "name", 2, "Unit", id, Status.ACTIVE)));

        eraser.delete(id);

        verify(itemEraser, atMostOnce()).delete(id);
        verify(itemRetriever, atLeastOnce()).retrieveListItems(id);
        verify(repository, atLeastOnce()).delete(id);
    }
}
