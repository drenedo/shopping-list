package me.renedo.lexical.term.aplication.create;

import static me.renedo.lexical.type.domain.Type.PRODUCT;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import me.renedo.lexical.term.domain.Term;
import me.renedo.lexical.term.domain.TermRepository;
import me.renedo.shopping.item.domain.event.ItemCreatedEvent;

class TermCreatorTest {

    @Test
    void save_new_term() {
        TermRepository repository = mock(TermRepository.class);
        TermCreator creator = new TermCreator(repository);
        ItemCreatedEvent itemCreatedEvent = new ItemCreatedEvent(UUID.randomUUID(), "some-name", null, null);

        creator.on(itemCreatedEvent);

        verify(repository, atLeastOnce()).save(any(Term.class));
    }

    @Test
    void update_term_when_supposed_optimistic_locking_happens() {
        TermRepository repository = mock(TermRepository.class);
        TermCreator creator = new TermCreator(repository);
        UUID id = UUID.randomUUID();
        LocalDateTime date = LocalDateTime.now();
        ItemCreatedEvent itemCreatedEvent = new ItemCreatedEvent(id, "some-name", null, null);
        Term term = new Term(id, "some-name", 2, PRODUCT, date);
        when(repository.find("some-name", PRODUCT)).thenReturn(Optional.of(term));
        when(repository.find(id)).thenReturn(Optional.of(term));

        creator.on(itemCreatedEvent);

        verify(repository, atLeast(2)).update(id, date, 3);
    }

    @Test
    void update_term() {
        TermRepository repository = mock(TermRepository.class);
        TermCreator creator = new TermCreator(repository);
        UUID id = UUID.randomUUID();
        LocalDateTime date = LocalDateTime.now();
        ItemCreatedEvent itemCreatedEvent = new ItemCreatedEvent(id, "some-name", null, null);
        Term term = new Term(id, "some-name", 2, PRODUCT, date);
        when(repository.find("some-name", PRODUCT)).thenReturn(Optional.of(term));
        when(repository.update(id, date, 3)).thenReturn(1);

        creator.on(itemCreatedEvent);

        verify(repository, atLeast(1)).update(id, date, 3);
    }
}
