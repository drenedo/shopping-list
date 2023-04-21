package me.renedo.payment.receipt.domain;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Test;

class ClassifierTest {

    @Test
    public void clean_text() {
        assertThat(Classifier.cleanNotDescriptiveCharacteres("áéo_irTyo"), is("aeoirtyo"));
    }
}
