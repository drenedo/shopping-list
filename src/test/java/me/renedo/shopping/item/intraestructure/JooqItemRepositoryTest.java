package me.renedo.shopping.item.intraestructure;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import me.renedo.InfrastructureTestCase;
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
    void save_item() {
        UUID uuid = UUID.randomUUID();

        Item saved = jooqItemRepository.save(new Item(uuid, "some-name", 23, "some-unit", "some-brand", givenIdOfEmptyList(), Status.ACTIVE));

        assertThat(saved, notNullValue());
        assertThat(saved.getId(), is(uuid));
    }

    @Test void find_item() {
        UUID uuid = UUID.randomUUID();
        jooqItemRepository.save(new Item(uuid, "some-name", 23, "some-unit", "some-brand", givenIdOfEmptyList(), Status.ACTIVE));

        Optional<Item> item = jooqItemRepository.findById(uuid);

        assertTrue(item.isPresent());
        assertThat(item.get().getId(), is(uuid));
    }

    @Test void delete_item() {
        UUID uuid = UUID.randomUUID();
        jooqItemRepository.save(new Item(uuid, "some-name", 23, "some-unit", "some-brand", givenIdOfEmptyList(), Status.ACTIVE));
        assertTrue(jooqItemRepository.findById(uuid).isPresent());

        jooqItemRepository.delete(uuid);

        assertFalse(jooqItemRepository.findById(uuid).isPresent());
    }

    @Test void update_status_of_item() {
        UUID uuid = UUID.fromString("d44f860a-0d91-4529-9f91-ac9f5f29a35c");

        jooqItemRepository.updateStatus(uuid, Status.INACTIVE);

        assertPresentAndStatusInactive(uuid);
    }

    @Test void update_all_items() {
        List<Item> items = givenListOfItems(UUID.fromString("d44f860a-0d91-4529-9f91-ac9f5f29a35c"));

        jooqItemRepository.updateStatus(items, Status.INACTIVE);

        assertPresentAndStatusInactive(UUID.fromString("d44f860a-0d91-4529-9f91-ac9f5f29a35c"));
    }

    @Test void find_non_exiting_item() {
        Optional<Item> item = jooqItemRepository.findById(UUID.randomUUID());

        assertFalse(item.isPresent());
    }

    private void assertPresentAndStatusInactive(UUID uuid) {
        Optional<Item> item = jooqItemRepository.findById(uuid);
        assertTrue(item.isPresent());
        assertThat(item.get().getStatus(), is(Status.INACTIVE));
    }

    private UUID givenIdOfEmptyList() {
        UUID uuid = UUID.randomUUID();
        shoppingListRepository.save(new ShoppingList(uuid, LocalDateTime.now(), "some-name", "some-description"));
        return uuid;
    }

    private List<Item> givenListOfItems(UUID... uids) {
        return Arrays.stream(uids).map(
                uuid -> new Item(uuid, "some-item", 1, "some-unit", "some-brand", UUID.fromString("d44f860a-0d91-4529-9f91-ac9f5f29a35c"),
                    Status.ACTIVE))
            .toList();
    }
}
