package me.renedo.lexical.term.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import me.renedo.lexical.type.domain.Type;

public interface TermRepository {

    Optional<Term> find(UUID id);

    Optional<Term> find(String name, Type type);

    List<Term> search(String text, Type type);

    Term save(Term term);

    int update(UUID id, LocalDateTime update, int times);

}
