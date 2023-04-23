package me.renedo.payment.statistic.infraestructure;

import static me.renedo.shopping.shared.jooq.tables.Receipt.RECEIPT;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.jooq.DSLContext;
import org.jooq.Record2;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;

import me.renedo.payment.statistic.domain.ReceiptSiteStatistic;
import me.renedo.payment.statistic.domain.ReceiptSiteStatisticRepository;
import me.renedo.shared.number.Money;

@Repository
public class JooqReceiptSiteStatisticRepository implements ReceiptSiteStatisticRepository {

    private final DSLContext context;

    public JooqReceiptSiteStatisticRepository(DSLContext context) {
        this.context = context;
    }

    @Override
    public List<ReceiptSiteStatistic> findByPeriod(LocalDateTime from, LocalDateTime to) {
        return context.select(RECEIPT.SITE, DSL.sum(RECEIPT.TOTAL))
            .from(RECEIPT)
            .where(RECEIPT.CREATED.between(from, to))
            .groupBy(RECEIPT.SITE)
            .orderBy(RECEIPT.SITE)
            .stream().map(this::toReceiptSiteStatistic).toList();
    }

    private ReceiptSiteStatistic toReceiptSiteStatistic(Record2<String, BigDecimal> record) {
        return new ReceiptSiteStatistic(record.value1(), new Money(record.value2().intValue()).getMoney());
    }
}
