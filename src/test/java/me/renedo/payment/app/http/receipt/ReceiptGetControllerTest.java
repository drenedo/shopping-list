package me.renedo.payment.app.http.receipt;

import java.util.List;

import org.junit.jupiter.api.Test;

import me.renedo.NoSecurityRequestTestCase;

class ReceiptGetControllerTest extends NoSecurityRequestTestCase {

    @Test
    void get_paginated() throws Exception {
        assertResponse("/api/v1/receipts/size/5", 200,
            givenObjectWithId(
                List.of("d44f860a-0d91-0529-9f91-ac9f5f29a35c", "d14f860a-0d91-0529-9f91-ac9f5f29a35c", "d14f860a-0d91-0529-9f91-ac9f5f29a45c",
                    "d14f860a-0d91-0529-9f91-ac9f5f27a45c")));
    }

    @Test
    void get_detail() throws Exception {
        assertResponse("/api/v1/receipts/detail/d44f860a-0d91-0529-9f91-ac9f5f29a35c", 200,
            givenObjectWithId("d44f860a-0d91-0529-9f91-ac9f5f29a35c"));
    }
}
