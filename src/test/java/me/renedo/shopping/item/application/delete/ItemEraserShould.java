package me.renedo.shopping.item.application.delete;

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.UUID;

import org.junit.jupiter.api.Test;

import me.renedo.shopping.item.domain.ItemRepository;

class ItemEraserShould {


    @Test
    public void delete_valid_item(){
        ItemRepository repository = mock(ItemRepository.class);
        ItemEraser eraser = new ItemEraser(repository);
        UUID id = UUID.randomUUID();

        eraser.delete(id);

        verify(repository, atLeastOnce()).delete(id);
    }
}
