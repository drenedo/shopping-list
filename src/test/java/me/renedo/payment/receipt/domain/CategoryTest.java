package me.renedo.payment.receipt.domain;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Test;

class CategoryTest {

    @Test
    public void test_value_of_id_with_food() {
        assertThat(Category.valueOfId("FOOD"), is(Category.FOOD));
    }

    @Test
    public void test_value_of_id_with_house() {
        assertThat(Category.valueOfId("H"), is(Category.HOUSE));
    }
}
