package me.renedo.lexical.term.infraestructure;

import static me.renedo.shopping.shared.jooq.tables.Term.TERM;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import me.renedo.lexical.term.domain.Term;
import me.renedo.lexical.term.domain.TermRepository;
import me.renedo.lexical.type.domain.Type;
import me.renedo.shopping.shared.jooq.tables.records.TermRecord;

@Repository
public class JooqTermRepository implements TermRepository {

    private final DSLContext context;

    public JooqTermRepository(DSLContext context) {
        this.context = context;
    }


    @Override public Optional<Term> find(UUID id) {
        return context.selectFrom(TERM)
            .where(TERM.ID.eq(id))
            .fetchOptional()
            .map(this::toTerm);
    }

    @Override
    public Optional<Term> find(String name, Type type) {
        return context.selectFrom(TERM)
            .where(TERM.NAME.eq(name).and(TERM.TYPE.eq(type.toString())))
            .fetchOptional()
            .map(this::toTerm);
    }

    @Override
    public List<Term> search(String text, Type type) {
        return context.selectFrom(TERM)
            .where(TERM.NAME.like(text+"%").and(TERM.TYPE.eq(type.toString())))
            .orderBy(TERM.TIMES.desc()).limit(10)
            .fetch()
            .map(this::toTerm);
    }

    @Override
    public Term save(Term term) {
        context.insertInto(TERM,
                TERM.ID, TERM.NAME, TERM.TIMES, TERM.TYPE, TERM.UPDATED)
            .values(term.getId(), term.getName(), term.getTimes(), String.valueOf(term.getType().getId()), term.getUpdated())
            .execute();
        return term;
    }

    @Override
    public int update(UUID id, LocalDateTime update, int times) {
        return context.update(TERM)
            .set(TERM.TIMES, times)
            .set(TERM.UPDATED, LocalDateTime.now())
            .where(TERM.ID.eq(id).and(TERM.UPDATED.eq(update)))
            .execute();
    }

    private Term toTerm(TermRecord termRecord) {
        return new Term(termRecord.getId(),termRecord.getName(), termRecord.getTimes(), Type.valueOfId(termRecord.getType()), termRecord.getUpdated());
    }
}
