package me.renedo.payment.line.domain;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import java.math.BigDecimal;
import java.util.UUID;

import org.junit.jupiter.api.Test;

class LineTest {

    @Test
    public void new_line_all_ok() {
        Line line = givenLine(new BigDecimal(10), 1D);

        assertThat(line.getName(), is("some-name"));
        assertThat(line.getTotal(), is(new BigDecimal(10)));
        assertThat(line.getCreated(), notNullValue());
    }

    @Test
    public void test_price() {
        Line line = givenLine(BigDecimal.valueOf(10), 0.92D);

        assertThat(line.getPrice(), is(10.87D));
    }

    @Test
    public void test_price_two() {
        Line line = givenLine(BigDecimal.valueOf(66.03), 256D);

        assertThat(line.getPrice(), is(0.258D));
    }

    private static Line givenLine(BigDecimal total, double amount) {
        Line line = new Line(UUID.randomUUID(), null, null, "some-name", total, amount, null);
        return line;
    }
}
