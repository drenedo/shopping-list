package me.renedo.shopping.item.application.create;

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.UUID;

import org.junit.jupiter.api.Test;

import me.renedo.shopping.item.domain.Item;
import me.renedo.shopping.item.domain.ItemRepository;
import me.renedo.shopping.status.domain.Status;

final class ItemCreatorShould {

    @Test
    public void save_a_valid_item(){
        ItemRepository repository = mock(ItemRepository.class);
        ItemCreator creator = new ItemCreator(repository);
        UUID id = UUID.randomUUID();
        Item item = new Item(id,"some-name", 10, "some-unit", null, Status.ACTIVE);

        creator.create(new ItemCreator.CreateItemRequest(id,"some-name", 10, "some-unit", null));

        verify(repository, atLeastOnce()).save(item);
    }
}
