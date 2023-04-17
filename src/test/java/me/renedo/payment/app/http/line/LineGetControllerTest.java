package me.renedo.payment.app.http.line;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;

import me.renedo.NoSecurityRequestTestCase;

class LineGetControllerTest extends NoSecurityRequestTestCase {

    @Test
    void search() throws Exception {
        assertResponse("/api/v1/line/search/name", 200,
            givenObjectWithId(
                List.of("d44f860a-0d91-0529-9f91-ac9f5f29a35c", "d44f860a-0d91-0539-9f91-ac9f5f29a35c", "d44f860a-0d91-0549-9f91-ac9f5f29a35c",
                    "d44f860a-0d91-0559-9f91-ac9f5f29a35c", "d44f860a-0d91-0569-9f91-ac9f5f29a35c", "d44f860a-0d91-0579-9f91-ac9f5f29a35c",
                    "d44f860a-0d91-0589-9f91-ac9f5f29a35c", "d44f860a-0d91-0599-9f91-ac9f5f29a35c")));
    }

}
