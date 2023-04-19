package me.renedo.payment.line.infraestructure;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import me.renedo.InfrastructureTestCase;
import me.renedo.payment.line.domain.Line;
import me.renedo.payment.line.domain.LinePrice;
import me.renedo.shared.uuid.UUIDValidator;

class JooqLineRepositoryTest extends InfrastructureTestCase {

    private final static UUID RECEIPT = UUIDValidator.fromString("d44f860a-0d91-0529-9f91-ac9f5f29a35c");

    private final JooqLineRepository jooqLineRepository;

    @Autowired
    JooqLineRepositoryTest(JooqLineRepository jooqLineRepository) {
        this.jooqLineRepository = jooqLineRepository;
    }

    @Test
    void save_line() {
        UUID uuid = UUID.randomUUID();
        UUID receipt = UUIDValidator.fromString("d14f860a-0d91-0529-9f91-ac9f5f27a45c");

        jooqLineRepository.save(new Line(uuid, null, null, "some-text", new BigDecimal(10), 1D, LocalDateTime.now()), receipt);

        List<Line> inReceipt = jooqLineRepository.findInReceipt(receipt);
        assertThat(inReceipt, hasSize(1));
        Line saved = inReceipt.get(0);
        assertThat(saved, notNullValue());
        assertThat(saved.getId(), is(uuid));
        assertThat(saved.getReceipt(), is(receipt));
    }

    @Test
    void search_lines(){
        List<LinePrice> lines = jooqLineRepository.search("NAME");

        assertThat(lines, hasSize(8));
    }

    @Test
    void find_lines_in_receipt(){
        List<Line> linesInReceipt = jooqLineRepository.findInReceipt(RECEIPT);

        assertThat(linesInReceipt, hasSize(8));
    }

    @Test
    void delete_line(){
        UUID receiptIdToDelete = UUIDValidator.fromString("d14f860a-0d91-0529-9f91-ac9f5f27a45c");
        List<Line> linesInReceipt = jooqLineRepository.findInReceipt(receiptIdToDelete);

        jooqLineRepository.delete(linesInReceipt.stream().map(Line::getId).toList());

        assertThat(jooqLineRepository.findInReceipt(receiptIdToDelete), hasSize(0));
    }
}
