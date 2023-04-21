package me.renedo.payment.receipt.infraestructure;

import static me.renedo.shopping.shared.jooq.tables.Classifier.CLASSIFIER;

import java.util.Optional;

import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import me.renedo.payment.receipt.domain.Category;
import me.renedo.payment.receipt.domain.Classifier;
import me.renedo.payment.receipt.domain.ClassifierRepository;
import me.renedo.shopping.shared.jooq.tables.records.ClassifierRecord;

@Repository
public class JooqClassifierRepository implements ClassifierRepository {

    private final DSLContext context;

    public JooqClassifierRepository(DSLContext context) {
        this.context = context;
    }

    @Override
    public Classifier save(Classifier classifier) {
        context.insertInto(CLASSIFIER,
                CLASSIFIER.ID, CLASSIFIER.TEXT, CLASSIFIER.CATEGORY)
            .values(classifier.getId(), classifier.getText(), classifier.getCategory() != null ? String.valueOf(classifier.getCategory().getId()) : null)
            .execute();
        return classifier;
    }

    @Override
    public Optional<Classifier> findByText(String text) {
        return context.selectFrom(CLASSIFIER)
            .where(CLASSIFIER.TEXT.eq(text))
            .fetchOptional().map(this::toClassifier);
    }

    @Override
    public Optional<Classifier> findByStartsWithText(String text) {
        return context.selectFrom(CLASSIFIER)
            .where(CLASSIFIER.TEXT.like(text + "%"))
            .limit(1)
            .fetchOptional().map(this::toClassifier);
    }

    private Classifier toClassifier(ClassifierRecord record) {
        return new Classifier(record.getId(), record.getText(), Category.valueOfId(record.getCategory()));
    }
}
