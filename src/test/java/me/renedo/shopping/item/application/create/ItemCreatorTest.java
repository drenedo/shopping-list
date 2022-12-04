package me.renedo.shopping.item.application.create;

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.atMostOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import me.renedo.shared.bus.EventBus;
import me.renedo.shopping.item.domain.Item;
import me.renedo.shopping.item.domain.ItemRepository;
import me.renedo.shopping.item.domain.event.ItemCreatedEvent;
import me.renedo.shopping.status.domain.Status;

final class ItemCreatorTest {

    @Test
    void save_a_valid_item(){
        ItemRepository repository = mock(ItemRepository.class);
        EventBus eventBus = mock(EventBus.class);
        ItemCreator creator = new ItemCreator(repository, eventBus);
        UUID id = UUID.randomUUID();
        UUID listId = UUID.randomUUID();
        Item item = new Item(id,"some-name", 10, "some-unit", "some-brand", listId, Status.ACTIVE);

        creator.create(new ItemCreator.CreateItemRequest(id,"some-name", 10, "some-unit", "some-brand", listId));

        verify(repository, atLeastOnce()).save(item);
        verify(eventBus, atMostOnce()).publish(List.of(new ItemCreatedEvent(item.getId(), item.getName(), item.getUnit(), item.getBrand())));
    }
}
