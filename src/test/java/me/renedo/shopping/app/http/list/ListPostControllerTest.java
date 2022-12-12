package me.renedo.shopping.app.http.list;

import org.junit.jupiter.api.Test;

import me.renedo.NoSecurityRequestTestCase;

class ListPostControllerTest extends NoSecurityRequestTestCase {

    @Test
    void update_state_of_list() throws Exception {
        assertRequest(POST, "/api/v1/lists/d44f860a-0d91-4529-9f91-ac9f5f29a39c/status/INACTIVE", 201);
    }
}
