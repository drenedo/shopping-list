package me.renedo.shopping.app.http.item;

import java.util.UUID;

import org.junit.jupiter.api.Test;

import me.renedo.shopping.RequestTestCase;

final class ItemPutControllerTest extends RequestTestCase {

    private final UUID LIST_ID = UUID.fromString("d44f860a-0d91-4529-9f91-ac9f5f29a39c");

    @Test
    void create_item() throws Exception {
        UUID uuid = UUID.randomUUID();

        assertRequestWithBody(PUT, "/api/v1/items/" + uuid, givenItem("some-name", "some-description", LIST_ID, 1, "some-unit"), 201);
    }

    @Test
    void create_bad_item() throws Exception {
        UUID uuid = UUID.randomUUID();

        assertRequestWithBody(PUT, "/api/v1/items/" + uuid, givenItem("some-name", "some-description", LIST_ID, null, null), 409);
    }

    @Test
    void create_not_valid_uuid() throws Exception {
        assertRequestWithBody(PUT, "/api/v1/items/not-valid-uuid", givenItem("some-name", "some-description", UUID.randomUUID(), null, null), 406);
    }
}
