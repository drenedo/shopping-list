package me.renedo.payment.receipt.infraestructure;

import static me.renedo.shopping.shared.jooq.tables.Receipt.RECEIPT;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import me.renedo.payment.receipt.domain.Category;
import me.renedo.payment.receipt.domain.Receipt;
import me.renedo.payment.receipt.domain.ReceiptRepository;
import me.renedo.shared.number.Money;
import me.renedo.shopping.shared.jooq.tables.records.ReceiptRecord;

@Repository
public class JooqReceiptRepository implements ReceiptRepository {
    private final DSLContext context;

    public JooqReceiptRepository(DSLContext context) {
        this.context = context;
    }

    @Override
    public Optional<Receipt> findById(UUID id) {
        return context.selectFrom(RECEIPT)
            .where(RECEIPT.ID.eq(id))
            .fetchOptional()
            .map(this::toReceipt);
    }

    @Override
    public boolean update(Receipt receipt) {
        return context.update(RECEIPT)
            .set(RECEIPT.SITE, receipt.getSite())
            .set(RECEIPT.TOTAL, new Money(receipt.getTotal()).getMoneyWithoutDecimals())
            .set(RECEIPT.CATEGORY, String.valueOf(receipt.getCategory().getId()))
            .set(RECEIPT.CASH, receipt.getCash())
            .where(RECEIPT.ID.eq(receipt.getId()))
            .execute() > 0;
    }

    @Override
    public Receipt save(Receipt receipt) {
        Money total = new Money(receipt.getTotal());
        context.insertInto(RECEIPT,
                RECEIPT.ID, RECEIPT.LIST, RECEIPT.CONTENT, RECEIPT.SITE, RECEIPT.TOTAL, RECEIPT.CREATED, RECEIPT.CASH, RECEIPT.LINE_NUMBER,
                RECEIPT.CATEGORY)
            .values(receipt.getId(), receipt.getList(), receipt.getText(), receipt.getSite(), total.getMoneyWithoutDecimals(), receipt.getCreated(),
                receipt.getCash(), receipt.getLineNumber(), receipt.getCategory() != null ? String.valueOf(receipt.getCategory().getId()) : null)
            .execute();
        return receipt;
    }

    @Override
    public List<Receipt> findAllPaginate(LocalDateTime time, int pageSize) {
        LocalDateTime notNullTime = time == null ? LocalDateTime.now() : time;
        return context.selectFrom(RECEIPT)
            .orderBy(RECEIPT.CREATED.desc())
            .seek(notNullTime)
            .limit(pageSize)
            .fetch()
            .map(this::toReceipt);
    }

    @Override
    public List<Receipt> findAllBetweenDates(LocalDateTime start, LocalDateTime end) {
        return context.selectFrom(RECEIPT)
            .where(RECEIPT.CREATED.between(start, end))
            .orderBy(RECEIPT.CREATED.desc())
            .fetch()
            .map(this::toReceipt);
    }

    @Override
    public int delete(UUID id) {
        return context.deleteFrom(RECEIPT)
            .where(RECEIPT.ID.eq(id))
            .execute();
    }

    private Receipt toReceipt(ReceiptRecord record) {
        return new Receipt(record.getId(), record.getList(), record.getContent(), new Money(record.getTotal()).getMoney(), record.getSite(), null,
            record.getCreated(), record.getCash(), record.getLineNumber(), Category.valueOfId(record.getCategory()));
    }
}
