package me.renedo.payment.receipt.infraestructure;

import static me.renedo.payment.receipt.domain.Category.HOUSE;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import me.renedo.InfrastructureTestCase;
import me.renedo.payment.receipt.domain.Classifier;

class JooqClassifierRepositoryTest extends InfrastructureTestCase {
    private final JooqClassifierRepository jooqClassifierRepository;

    @Autowired
    JooqClassifierRepositoryTest(JooqClassifierRepository jooqClassifierRepository) {
        this.jooqClassifierRepository = jooqClassifierRepository;
    }

    @Test
    void save_receipt() {
        UUID uuid = UUID.randomUUID();

        Classifier saved =
            jooqClassifierRepository.save(
                new Classifier(uuid, "some-text", HOUSE));

        assertThat(saved, notNullValue());
        assertThat(saved.getId(), is(uuid));
        assertThat(saved.getCategory(), is(HOUSE));
    }

}
