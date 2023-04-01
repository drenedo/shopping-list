package me.renedo.shopping.list.application.retrieve;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.atMostOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import me.renedo.shopping.item.application.retrieve.ItemRetriever;
import me.renedo.shopping.list.domain.ShoppingListRepository;

class ShoppingListRetrieverTestTest {

    @Test
    void retrieve_paginated(){
        ShoppingListRepository repository = mock(ShoppingListRepository.class);
        ItemRetriever itemRetriever = mock(ItemRetriever.class);
        ShoppingListRetriever retriever = new ShoppingListRetriever(repository, itemRetriever);

        retriever.retrievePaginated(null, 5);

        verify(repository, atMostOnce()).findAllPaginate(any(LocalDateTime.class), eq(5));
    }

    @Test
    void retrieve_paginated_with_date(){
        ShoppingListRepository repository = mock(ShoppingListRepository.class);
        ItemRetriever itemRetriever = mock(ItemRetriever.class);
        ShoppingListRetriever retriever = new ShoppingListRetriever(repository, itemRetriever);
        LocalDateTime date = LocalDateTime.of(2021, 1, 1, 20, 23);

        retriever.retrievePaginated(date, 5);

        verify(repository, atMostOnce()).findAllPaginate(date, 5);
    }
}
