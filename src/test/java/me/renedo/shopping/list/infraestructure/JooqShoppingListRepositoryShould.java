package me.renedo.shopping.list.infraestructure;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import me.renedo.shopping.InfraestructureShould;
import me.renedo.shopping.list.domain.ShoppingList;
import me.renedo.shopping.status.domain.Status;

class JooqShoppingListRepositoryShould extends InfraestructureShould {

    private final JooqShoppingListRepository repository;

    @Autowired
    JooqShoppingListRepositoryShould(JooqShoppingListRepository repository) {
        this.repository = repository;
    }

    @Test
    public void find_first_elements_paginated(){
        List<ShoppingList> lists = repository.findAllPaginate(null, 5);

        assertThat(lists, Matchers.hasSize(5));
        assertThat(lists.get(0).getId().toString(), is("d44f860a-0d91-4529-9f91-ac9f5f29a340"));
        assertThat(lists.get(1).getId().toString(), is("d44f860a-0d91-4529-9f91-ac9f5f29a34a"));
        assertThat(lists.get(2).getId().toString(), is("d44f860a-0d91-4529-9f91-ac9f5f29a33a"));
        assertThat(lists.get(3).getId().toString(), is("d44f860a-0d91-4529-9f91-ac9f5f29a32a"));
        assertThat(lists.get(4).getId().toString(), is("d44f860a-0d91-4529-9f91-ac9f5f29a31a"));
    }

    @Test
    public void find_second_elements_paginated(){
        List<ShoppingList> lists = repository.findAllPaginate(LocalDateTime.of(2022, 11, 16, 10, 0, 1), 3);

        assertThat(lists, Matchers.hasSize(3));
        assertThat(lists.get(0).getId().toString(), is("d44f860a-0d91-4529-9f91-ac9f5f29a340"));
        assertThat(lists.get(1).getId().toString(), is("d44f860a-0d91-4529-9f91-ac9f5f29a34a"));
        assertThat(lists.get(2).getId().toString(), is("d44f860a-0d91-4529-9f91-ac9f5f29a33a"));
    }

    @Test
    public void save_list(){
        UUID uuid = UUID.randomUUID();

        ShoppingList saved = repository.save(new ShoppingList(uuid, LocalDateTime.now(), "some-name", "some-descriotion", null, Status.ACTIVE));

        assertThat(saved, notNullValue());
        assertThat(saved.getId(), is(uuid));
    }

    @Test
    public void find_list(){
        UUID uuid = UUID.randomUUID();
        repository.save(new ShoppingList(uuid, LocalDateTime.now(), "some-name", "some-descriotion", null, Status.ACTIVE));

        Optional<ShoppingList> item = repository.findById(uuid);

        assertTrue(item.isPresent());
        assertThat(item.get().getId(), is(uuid));
    }

    @Test
    public void delete_list(){
        UUID uuid = UUID.randomUUID();
        repository.save(new ShoppingList(uuid, LocalDateTime.now(), "some-name", "some-descriotion", null, Status.ACTIVE));
        assertTrue(repository.findById(uuid).isPresent());

        repository.delete(uuid);

        assertFalse(repository.findById(uuid).isPresent());

    }
}
