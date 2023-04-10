package me.renedo.payment.receipt.infraestructure;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import me.renedo.InfrastructureTestCase;
import me.renedo.payment.receipt.domain.Receipt;
import me.renedo.shared.uuid.UUIDValidator;

class JooqReceiptRepositoryTest extends InfrastructureTestCase {
    private final JooqReceiptRepository jooqReceiptRepository;

    @Autowired JooqReceiptRepositoryTest(JooqReceiptRepository jooqReceiptRepository) {
        this.jooqReceiptRepository = jooqReceiptRepository;
    }

    @Test
    void save_receipt() {
        UUID uuid = UUID.randomUUID();

        Receipt saved =
            jooqReceiptRepository.save(new Receipt(uuid, null, "some-text", new BigDecimal(10), "some-site", null, LocalDateTime.now(), true));

        assertThat(saved, notNullValue());
        assertThat(saved.getId(), is(uuid));
    }

    @Test
    void find_paginate_all() {
        List<Receipt> receipts = jooqReceiptRepository.findAllPaginate(LocalDateTime.of(2023, 2, 16, 14, 0), 10);

        assertThat(receipts.size(), is(4));
        assertThat(receipts.get(0).getText(), is("ALCAMPO 4"));
        assertThat(receipts.get(1).getText(), is("ALCAMPO 3"));
        assertThat(receipts.get(2).getText(), is("ALCAMPO 2"));
        assertThat(receipts.get(3).getText(), is("ALCAMPO 1"));
    }

    @Test
    void find_paginate_page() {
        List<Receipt> receipts = jooqReceiptRepository.findAllPaginate(LocalDateTime.of(2023, 2, 16, 12, 0), 10);

        assertThat(receipts.size(), is(2));
        assertThat(receipts.get(0).getText(), is("ALCAMPO 2"));
        assertThat(receipts.get(1).getText(), is("ALCAMPO 1"));
    }

    @Test
    void find_paginate_none() {
        List<Receipt> receipts = jooqReceiptRepository.findAllPaginate(LocalDateTime.of(2023, 2, 14, 12, 0), 10);

        assertThat(receipts.size(), is(0));
    }

    @Test
    void find_between_dates() {
        List<Receipt> receipts = jooqReceiptRepository.findAllBetweenDates(LocalDateTime.of(2023, 2, 16, 0, 0), LocalDateTime.of(2023, 2, 16, 23, 59));

        assertThat(receipts.size(), is(4));
    }

    @Test
    void delete_receipt() {
        assertThat(jooqReceiptRepository.delete(UUID.randomUUID()), is(0));
    }

    @Test
    void find_by_id() {
        assertThat(jooqReceiptRepository.findById(UUIDValidator.fromString("d44f860a-0d91-0529-9f91-ac9f5f29a35c")), notNullValue());
    }
}
