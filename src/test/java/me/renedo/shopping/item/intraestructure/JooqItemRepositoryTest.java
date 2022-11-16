package me.renedo.shopping.item.intraestructure;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import me.renedo.shopping.InfrastructureTestCase;
import me.renedo.shopping.item.domain.Item;
import me.renedo.shopping.list.domain.ShoppingList;
import me.renedo.shopping.list.infraestructure.JooqShoppingListRepository;
import me.renedo.shopping.status.domain.Status;

final class JooqItemRepositoryTest extends InfrastructureTestCase {

    private final JooqItemRepository jooqItemRepository;

    private final JooqShoppingListRepository shoppingListRepository;

    @Autowired
    JooqItemRepositoryTest(JooqItemRepository jooqItemRepository, JooqShoppingListRepository shoppingListRepository) {
        this.jooqItemRepository = jooqItemRepository;
        this.shoppingListRepository = shoppingListRepository;
    }

    @Test
    void save_item(){
        UUID uuid = UUID.randomUUID();

        Item saved = jooqItemRepository.save(new Item(uuid, "some-name", 23, "some-unit", givenIdOfEmptyList(), Status.ACTIVE));

        assertThat(saved, notNullValue());
        assertThat(saved.getId(), is(uuid));
    }

    @Test
    void find_item(){
        UUID uuid = UUID.randomUUID();
        jooqItemRepository.save(new Item(uuid, "some-name", 23, "some-unit", givenIdOfEmptyList(), Status.ACTIVE));

        Optional<Item> item = jooqItemRepository.findById(uuid);

        assertTrue(item.isPresent());
        assertThat(item.get().getId(), is(uuid));
    }

    @Test
    void delete_item(){
        UUID uuid = UUID.randomUUID();
        jooqItemRepository.save(new Item(uuid, "some-name", 23, "some-unit", givenIdOfEmptyList(), Status.ACTIVE));
        assertTrue( jooqItemRepository.findById(uuid).isPresent());

        jooqItemRepository.delete(uuid);

        assertFalse( jooqItemRepository.findById(uuid).isPresent());
    }

    @Test
    void find_non_exiting_item(){
        Optional<Item> item = jooqItemRepository.findById(UUID.randomUUID());

        assertFalse(item.isPresent());
    }

    private UUID givenIdOfEmptyList(){
        UUID uuid = UUID.randomUUID();
        shoppingListRepository.save(new ShoppingList(uuid, LocalDateTime.now(), "some-name", "some-description"));
        return uuid;
    }
}
