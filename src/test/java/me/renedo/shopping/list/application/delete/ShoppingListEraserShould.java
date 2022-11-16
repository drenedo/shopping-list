package me.renedo.shopping.list.application.delete;

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.UUID;

import org.junit.jupiter.api.Test;

import me.renedo.shopping.list.domain.ShoppingListRepository;

class ShoppingListEraserShould {


    @Test
    public void delete_item(){
        ShoppingListRepository repository = mock(ShoppingListRepository.class);
        ShoppingListEraser eraser = new ShoppingListEraser(repository);
        UUID id = UUID.randomUUID();

        eraser.delete(id);

        verify(repository, atLeastOnce()).delete(id);
    }

}
