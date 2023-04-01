package me.renedo.payment.app.http.receipt;

import java.util.UUID;

import org.junit.jupiter.api.Test;

import me.renedo.NoSecurityRequestTestCase;

class ReceiptPutControllerTest extends NoSecurityRequestTestCase {

    @Test
    void create_receipt() throws Exception {
        UUID uuid = UUID.randomUUID();

        assertRequestWithBody(PUT, "/api/v1/receipt/" + uuid, givenImage("tickets/ticket_alcampo.jpeg", "alcampo.jpg"), 201);
    }
}
