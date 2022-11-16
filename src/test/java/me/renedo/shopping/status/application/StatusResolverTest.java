package me.renedo.shopping.status.application;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;

import java.util.List;

import org.junit.jupiter.api.Test;

import me.renedo.shopping.status.domain.Status;

class StatusResolverTest {

    @Test
    void save_a_valid_item(){
        StatusResolver resolver = new StatusResolver();

        List<Status> statuses = resolver.getAllStatus();

        assertThat(statuses, hasSize(3));
        assertThat(statuses, containsInAnyOrder(Status.ACTIVE, Status.CANCELED, Status.INACTIVE));
    }
}
