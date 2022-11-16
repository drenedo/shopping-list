package me.renedo.shopping.app.http.list;

import org.junit.jupiter.api.Test;

import me.renedo.shopping.RequestTestCase;

class ListDeleteControllerTest extends RequestTestCase {

    @Test
    void delete_list() throws Exception {
        assertRequest(DELETE, "/api/v1/items/d44f860a-0d91-4529-9f91-ac9f5f29a39c", 200);
    }
}
