package me.renedo.payment.statistic.infraestructure;

import static me.renedo.shopping.shared.jooq.tables.Receipt.RECEIPT;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.jooq.DSLContext;
import org.jooq.Record2;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;

import me.renedo.payment.receipt.domain.Category;
import me.renedo.payment.statistic.domain.ReceiptCategoryStatistic;
import me.renedo.payment.statistic.domain.ReceiptCategoryStatisticRepository;

@Repository
public class JooqReceiptCategoryStatisticRepository implements ReceiptCategoryStatisticRepository {

    private final DSLContext context;

    public JooqReceiptCategoryStatisticRepository(DSLContext context) {
        this.context = context;
    }

    @Override
    public List<ReceiptCategoryStatistic> findByPeriod(LocalDateTime from, LocalDateTime to) {
        return context.select(RECEIPT.CATEGORY, DSL.sum(RECEIPT.TOTAL))
            .from(RECEIPT)
            .where(RECEIPT.CREATED.between(from, to))
            .groupBy(RECEIPT.CATEGORY)
            .orderBy(RECEIPT.CATEGORY)
            .stream().map(this::toReceiptCategoryStatistic).toList();
    }

    private ReceiptCategoryStatistic toReceiptCategoryStatistic(Record2<String, BigDecimal> record) {
        return new ReceiptCategoryStatistic(Category.valueOfId(record.value1()), record.value2().doubleValue());
    }
}
