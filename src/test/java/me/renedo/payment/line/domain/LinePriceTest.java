package me.renedo.payment.line.domain;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import java.math.BigDecimal;
import java.util.UUID;

import org.junit.jupiter.api.Test;

class LinePriceTest {

    @Test
    public void new_line_all_ok() {
        LinePrice linePrice = givenLinePrice(new BigDecimal(10), 1D);

        assertThat(linePrice.getName(), is("some-name"));
        assertThat(linePrice.getTotal(), is(new BigDecimal(10)));
        assertThat(linePrice.getPrice(), is(10D));
    }

    @Test
    public void calculate_price() {
        LinePrice linePrice = givenLinePrice(new BigDecimal(234.2D), 78.4D);

        assertThat(linePrice.getPrice(), is(2.987D));
    }

    private static LinePrice givenLinePrice(BigDecimal total, Double amount) {
       return new LinePrice(UUID.randomUUID(), "some-name", "some-site", total, null, amount);
    }

}
