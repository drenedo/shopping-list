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
        Line line = new Line(UUID.randomUUID(), null, null, "some-name", new BigDecimal(10), 1, null);

        assertThat(line.getName(), is("some-name"));
        assertThat(line.getTotal(), is(new BigDecimal(10)));
        assertThat(line.getCreated(), notNullValue());
    }

}
