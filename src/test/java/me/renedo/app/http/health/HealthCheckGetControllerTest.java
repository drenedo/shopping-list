package me.renedo.app.http.health;


import org.junit.jupiter.api.Test;

import me.renedo.NoSecurityRequestTestCase;

final class HealthCheckGetControllerTest extends NoSecurityRequestTestCase {

    @Test
    void health_check_should_return_ok() throws Exception{
        assertResponse("/health", 200, "{'status':'ok'}");
    }

}
