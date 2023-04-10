package me.renedo.payment.receipt.domain;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import me.renedo.shared.exception.NotAcceptableException;

class ReceiptTest {

    @Test
    public void new_receipt_all_ok() {
        Receipt receipt = new Receipt(UUID.randomUUID(), null, "some-text", new BigDecimal(10), "some-site", null, LocalDateTime.now(), true);

        assertThat(receipt.getText(), is("some-text"));
        assertThat(receipt.getSite(), is("some-site"));
        assertThat(receipt.getTotal(), is(new BigDecimal((10))));
    }

    @Test
    public void id_is_null() {
        assertThrows(NotAcceptableException.class,
            () -> new Receipt(null, null, "some-text", new BigDecimal(10), "some-site", null, LocalDateTime.now(), true));
    }

    @Test
    public void text_is_null() {
        assertThrows(NotAcceptableException.class,
            () -> new Receipt(UUID.randomUUID(), null, null, new BigDecimal(10), "some-site", null, LocalDateTime.now(), true));
    }

    @Test
    public void total_is_null() {
        assertThrows(NotAcceptableException.class,
            () -> new Receipt(UUID.randomUUID(), null, "some-text", null, "some-site", null, LocalDateTime.now(), true));
    }

    @Test
    public void site_is_null() {
        assertThrows(NotAcceptableException.class,
            () -> new Receipt(UUID.randomUUID(), null, "some-text", new BigDecimal(10), null, null, LocalDateTime.now(), true));
    }
}
