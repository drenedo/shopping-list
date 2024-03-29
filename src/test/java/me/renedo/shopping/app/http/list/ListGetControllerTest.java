package me.renedo.shopping.app.http.list;

import java.util.List;

import org.junit.jupiter.api.Test;

import me.renedo.NoSecurityRequestTestCase;

class ListGetControllerTest extends NoSecurityRequestTestCase {

    @Test
    void get_paginated() throws Exception {
        assertResponse("/api/v1/lists/size/5", 200,
            givenObjectWithId(
                List.of("d44f860a-0d91-4529-9f91-ac9f5f29a340", "d44f860a-0d91-4529-9f91-ac9f5f29a34a", "d44f860a-0d91-4529-9f91-ac9f5f29a33a",
                    "d44f860a-0d91-4529-9f91-ac9f5f29a32a", "d44f860a-0d91-4529-9f91-ac9f5f29a31a")));
    }

}
