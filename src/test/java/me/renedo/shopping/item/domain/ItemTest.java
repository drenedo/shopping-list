package me.renedo.shopping.item.domain;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.UUID;

import org.junit.jupiter.api.Test;

import me.renedo.shared.exception.NotAcceptableException;
import me.renedo.shopping.status.domain.Status;

class ItemTest {

    @Test
    public void convert_string_to_lower_case() {
        Item item = new Item(UUID.randomUUID(), "Name", 12, "Unit", "Some BRAND", UUID.randomUUID(), Status.ACTIVE);

        assertThat(item.getBrand(), is("some brand"));
        assertThat(item.getUnit(), is("unit"));
        assertThat(item.getName(), is("name"));
    }

    @Test
    public void convert_string_to_lower_case_and_unit_is_null() {
        Item item = new Item(UUID.randomUUID(), "Name", 12, null, "Some BRAND", UUID.randomUUID(), Status.ACTIVE);

        assertThat(item.getBrand(), is("some brand"));
        assertThat(item.getUnit(), nullValue());
        assertThat(item.getName(), is("name"));
    }

    @Test
    public void id_is_null() {
        assertThrows(NotAcceptableException.class, () -> new Item(null, "Name", 12, null, "Some BRAND", UUID.randomUUID(), Status.ACTIVE));
    }

    @Test
    public void list_id_is_null() {
        assertThrows(NotAcceptableException.class, () -> new Item(UUID.randomUUID(), "Name", 12, null, "Some BRAND", null, Status.ACTIVE));
    }

    @Test
    public void name_is_empty() {
        assertThrows(NotAcceptableException.class, () -> new Item(UUID.randomUUID(), "", 12, null, "Some BRAND", UUID.randomUUID(), Status.ACTIVE));
    }

    @Test
    public void name_is_null() {
        assertThrows(NotAcceptableException.class, () -> new Item(UUID.randomUUID(), null, 12, null, "Some BRAND", UUID.randomUUID(), Status.ACTIVE));
    }

    @Test
    public void amount_is_zero() {
        assertThrows(NotAcceptableException.class, () -> new Item(UUID.randomUUID(), "name", 0, null, null, UUID.randomUUID(), Status.ACTIVE));
    }
}
