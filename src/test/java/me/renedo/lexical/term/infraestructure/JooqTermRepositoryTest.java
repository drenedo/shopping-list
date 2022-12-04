package me.renedo.lexical.term.infraestructure;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import me.renedo.InfrastructureTestCase;
import me.renedo.lexical.term.domain.Term;
import me.renedo.lexical.type.domain.Type;

class JooqTermRepositoryTest extends InfrastructureTestCase {

    private final JooqTermRepository repository;

    @Autowired
    JooqTermRepositoryTest(JooqTermRepository jooqTermRepository) {
        this.repository = jooqTermRepository;
    }

    @Test
    void save_term() {
        UUID uuid = UUID.randomUUID();

        Term saved = repository.save(new Term(uuid, "some-name", 23, Type.PRODUCT, LocalDateTime.now()));

        assertThat(saved, notNullValue());
        assertThat(saved.getId(), is(uuid));
    }

    @Test
    void update_term_wrong_date() {
        assertThat(repository.update(UUID.fromString("d44f860a-0d91-4529-9f91-ac9f5f29a35c"), LocalDateTime.now(), 5), is(0));
    }

    @Test
    void update_term_right_date() {
        Optional<Term> term = repository.find("item", Type.PRODUCT);
        assertTrue(term.isPresent());

        int updated = repository.update(term.get().getId(), term.get().getUpdated(), 5);

        assertThat(updated, is(1));
        Optional<Term> updatedTerm = repository.find("item", Type.PRODUCT);
        assertTrue(updatedTerm.isPresent());
        assertThat(updatedTerm.get().getUpdated(), not(term.get().getUpdated()));
    }

    @Test
    void search() {
        List<Term> ite = repository.search("ite", Type.PRODUCT);

        assertThat(ite, Matchers.hasSize(2));
        assertThat(ite.get(0).getName(), is("item"));
        assertThat(ite.get(1).getName(), is("itelo"));
    }
}
