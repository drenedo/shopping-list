package me.renedo.shopping.app.http.item;

import org.junit.jupiter.api.Test;

import me.renedo.RequestTestCase;

class ItemDeleteControllerTest extends RequestTestCase {

    @Test
    void delete_item() throws Exception {
        assertRequest(DELETE, "/api/v1/items/d44f860a-0d91-4529-9f91-ac9f5f29a35c", 200);
    }

}
