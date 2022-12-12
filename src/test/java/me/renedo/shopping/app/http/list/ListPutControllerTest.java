package me.renedo.shopping.app.http.list;

import java.util.UUID;

import org.junit.jupiter.api.Test;

import me.renedo.NoSecurityRequestTestCase;

final class ListPutControllerTest extends NoSecurityRequestTestCase {

    @Test
    void create_list() throws Exception {
        UUID uuid = UUID.randomUUID();

        assertRequestWithBody(PUT, "/api/v1/lists/" + uuid, givenShoppingList("some-name", "some-description"), 201);
    }

    @Test
    void create_bad_list() throws Exception {
        UUID uuid = UUID.randomUUID();

        assertRequestWithBody(PUT, "/api/v1/lists/" + uuid, givenShoppingList(null, null), 409);
    }

    @Test
    void create_list_not_valid_uuid() throws Exception {
        assertRequestWithBody(PUT, "/api/v1/lists/not-valid", givenShoppingList("some-name", "some-description"), 406);
    }
}
