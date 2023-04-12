package me.renedo.payment.receipt.domain;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import me.renedo.payment.line.domain.Line;
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

    @Test
    public void test_sum_of_elements() {
        Receipt receipt = givenReceipt(33D, List.of(11D, 9D, 7D, 10D, 3D));

        List<Line> lines = receipt.findCorrectSum();

        assertThat(lines, hasSize(4));
        assertThat(lines.stream().mapToDouble(l -> l.getTotal().doubleValue()).sum(), is(33D));
    }

    @Test
    public void test_sum_of_elements_more_deeply() {
        Receipt receipt = givenReceipt(9D, List.of(1D, 2D, 3D, 4D, 5D));

        List<Line> lines = receipt.findCorrectSum();

        assertThat(lines, hasSize(3));
        assertThat(lines.stream().mapToDouble(l -> l.getTotal().doubleValue()).sum(), is(9D));
    }

    @Test
    public void test_sum_of_elements_more_deeply_2() {
        Receipt receipt = givenReceipt(9D, List.of(30D, 2D, 4D, 4D, 5D));

        List<Line> lines = receipt.findCorrectSum();

        assertThat(lines, hasSize(2));
        assertThat(lines.stream().mapToDouble(l -> l.getTotal().doubleValue()).sum(), is(9D));
    }

    @Test
    public void test_sum_of_elements_more_deeply_3() {
        Receipt receipt = givenReceipt(19D, List.of(30D, 2D, 4D, 4D, 5D, 10D));

        List<Line> lines = receipt.findCorrectSum();

        assertThat(lines, hasSize(3));
        assertThat(lines.stream().mapToDouble(l -> l.getTotal().doubleValue()).sum(), is(19D));
    }

    @Test
    public void test_impossible_sum() {
        Receipt receipt = givenReceipt(109D, List.of(30D, 2D, 4D, 4D, 5D, 10D));

        assertThrows(NotAcceptableException.class, receipt::findCorrectSum);
    }

    private Receipt givenReceipt(double total, List<Double> lines) {
        return new Receipt(UUID.randomUUID(), UUID.randomUUID(), "some-text", BigDecimal.valueOf(total), "some-site",
            lines.stream().map(d -> new Line(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), "some-name", BigDecimal.valueOf(d), 1, null))
                .toList(), null, null);
    }
}
