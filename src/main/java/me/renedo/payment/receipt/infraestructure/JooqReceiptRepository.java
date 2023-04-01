package me.renedo.payment.receipt.infraestructure;

import static me.renedo.shopping.shared.jooq.tables.Receipt.RECEIPT;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import me.renedo.payment.receipt.domain.Receipt;
import me.renedo.payment.receipt.domain.ReceiptRepository;
import me.renedo.shared.money.Money;
import me.renedo.shopping.shared.jooq.tables.records.ReceiptRecord;

@Repository
public class JooqReceiptRepository implements ReceiptRepository {
    private final DSLContext context;

    public JooqReceiptRepository(DSLContext context) {
        this.context = context;
    }

    @Override public
    Optional<Receipt> findById(UUID id) {
        return context.selectFrom(RECEIPT)
            .where(RECEIPT.ID.eq(id))
            .fetchOptional()
            .map(this::toReceipt);
    }

    @Override
    public Receipt save(Receipt receipt) {
        Money total = new Money(receipt.getTotal());
        context.insertInto(RECEIPT,
                RECEIPT.ID, RECEIPT.LIST, RECEIPT.CONTENT, RECEIPT.SITE, RECEIPT.TOTAL, RECEIPT.CREATED)
            .values(receipt.getId(), receipt.getList(), receipt.getText(), receipt.getSite(), total.getMoneyWithoutDecimals(), receipt.getCreated())
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
    public int delete(UUID id) {
        return context.deleteFrom(RECEIPT)
            .where(RECEIPT.ID.eq(id))
            .execute();
    }

    private Receipt toReceipt(ReceiptRecord record) {
        return new Receipt(record.getId(), record.getList(), record.getContent(), new Money(record.getTotal()).getMoney(), record.getSite(), null,
            record.getCreated());
    }
}
