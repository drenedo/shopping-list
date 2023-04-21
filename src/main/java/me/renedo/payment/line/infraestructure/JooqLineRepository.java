package me.renedo.payment.line.infraestructure;

import static me.renedo.shopping.shared.jooq.tables.Line.LINE;
import static me.renedo.shopping.shared.jooq.tables.Receipt.RECEIPT;
import static org.jooq.impl.DSL.lower;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.jooq.DSLContext;
import org.jooq.Record6;
import org.springframework.stereotype.Repository;

import me.renedo.payment.line.domain.Line;
import me.renedo.payment.line.domain.LinePrice;
import me.renedo.payment.line.domain.LineRepository;
import me.renedo.shared.number.Amount;
import me.renedo.shared.number.Money;
import me.renedo.shopping.shared.jooq.tables.records.LineRecord;

@Repository
public class JooqLineRepository implements LineRepository {
    private final DSLContext context;

    public JooqLineRepository(DSLContext context) {
        this.context = context;
    }

    @Override
    public Line save(Line line, UUID receipt) {
        Money total = new Money(line.getTotal());
        Amount amount = new Amount(line.getAmount());
        context.insertInto(LINE,
                LINE.ID, LINE.RECEIPT, LINE.ITEM, LINE.AMOUNT, LINE.NAME, LINE.TOTAL, LINE.CREATED)
            .values(line.getId(), receipt, line.getItem(), amount.getAmountWithoutDecimals(), line.getName(), total.getMoneyWithoutDecimals(),
                line.getCreated())
            .execute();
        return line;
    }

    @Override public boolean update(Line line) {
        return context.update(LINE)
            .set(LINE.NAME, line.getName())
            .set(LINE.TOTAL, new Money(line.getTotal()).getMoneyWithoutDecimals())
            .where(LINE.ID.eq(line.getId()))
            .execute() > 0;
    }

    @Override
    public List<Line> findInReceipt(UUID receiptId) {
        return context.selectFrom(LINE)
            .where(LINE.RECEIPT.eq(receiptId))
            .fetch()
            .map(this::toLine);
    }

    @Override
    public List<LinePrice> search(String text) {
        //TODO this has a bad performance, if there are a lot of data we need to considere another option.
        return context.select(LINE.ID, LINE.NAME, RECEIPT.SITE, LINE.TOTAL, LINE.CREATED, LINE.AMOUNT)
            .from(LINE)
            .leftJoin(RECEIPT).on(RECEIPT.ID.eq(LINE.RECEIPT))
            .where(lower(LINE.NAME).like("%" + text.toLowerCase() + "%"))
            .orderBy(LINE.CREATED).limit(20)
            .fetch()
            .map(this::toLinePrice);
    }

    private LinePrice toLinePrice(Record6<UUID, String, String, Integer, LocalDateTime, Integer> record) {
        return new LinePrice(record.value1(), record.value2(), record.value3(), new Money(record.value4()).getMoney(), record.value5(),
            new Amount(record.value6()).getAmount());
    }

    @Override
    public int delete(List<UUID> ids) {
        return context.deleteFrom(LINE)
            .where(LINE.ID.in(ids)).execute();
    }

    private Line toLine(LineRecord record) {
        Money money = new Money(record.getTotal());
        Amount amount = new Amount(record.getAmount());
        return new Line(record.getId(), record.getReceipt(), record.getItem(), record.getName(), money.getMoney(), amount.getAmount(),
            record.getCreated());
    }
}
