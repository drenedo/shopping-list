package me.renedo.payment.line.infraestructure;

import static me.renedo.shopping.shared.jooq.tables.Line.LINE;

import java.util.List;
import java.util.UUID;

import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import me.renedo.payment.line.domain.Line;
import me.renedo.payment.line.domain.LineRepository;
import me.renedo.shared.money.Money;
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
        context.insertInto(LINE,
                LINE.ID, LINE.RECEIPT, LINE.ITEM, LINE.AMOUNT, LINE.NAME, LINE.TOTAL, LINE.CREATED)
            .values(line.getId(), receipt, line.getItem(), line.getAmount(), line.getName(), total.getMoneyWithoutDecimals(), line.getCreated())
            .execute();
        return line;
    }

    @Override
    public List<Line> findInReceipt(UUID receiptId) {
        return context.selectFrom(LINE)
            .where(LINE.RECEIPT.eq(receiptId))
            .fetch()
            .map(this::toLine);
    }

    @Override
    public int delete(List<UUID> ids) {
        return context.deleteFrom(LINE)
            .where(LINE.ID.in(ids)).execute();
    }

    private Line toLine(LineRecord record) {
        Money money = new Money(record.getTotal());
        return new Line(record.getId(), record.getReceipt(), record.getItem(), record.getName(), money.getMoney(), record.getAmount(),
            record.getCreated());
    }
}
