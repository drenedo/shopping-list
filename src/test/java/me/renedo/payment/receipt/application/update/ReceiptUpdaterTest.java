package me.renedo.payment.receipt.application.update;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atMostOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import me.renedo.payment.line.domain.Line;
import me.renedo.payment.line.domain.LineRepository;
import me.renedo.payment.receipt.application.update.ReceiptUpdater.UpdateLineRequest;
import me.renedo.payment.receipt.application.update.ReceiptUpdater.UpdateReceiptRequest;
import me.renedo.payment.receipt.domain.Receipt;
import me.renedo.payment.receipt.domain.ReceiptRepository;
import me.renedo.shared.exception.NotFoundException;

class ReceiptUpdaterTest {

    @Test
    public void update_simple_receipt() {
        ReceiptRepository repository = mock(ReceiptRepository.class);
        LineRepository lineRepository = mock(LineRepository.class);
        ReceiptUpdater updater = new ReceiptUpdater(repository, lineRepository);
        UpdateReceiptRequest receipt = givenUpdateRequest();
        when(repository.update(any(Receipt.class))).thenReturn(true);

        updater.update(receipt);

        verify(lineRepository, atMostOnce()).update(receipt.toReceipt().getLines().get(0));
    }

    @Test
    public void update_receipt_does_not_exist() {
        ReceiptRepository repository = mock(ReceiptRepository.class);
        LineRepository lineRepository = mock(LineRepository.class);
        ReceiptUpdater updater = new ReceiptUpdater(repository, lineRepository);
        UpdateReceiptRequest receipt = givenUpdateRequest();
        when(repository.update(receipt.toReceipt())).thenReturn(false);

        assertThrows(NotFoundException.class, () -> updater.update(receipt));
    }


    @Test
    public void update_complex_receipt() {
        ReceiptRepository repository = mock(ReceiptRepository.class);
        LineRepository lineRepository = mock(LineRepository.class);
        ReceiptUpdater updater = new ReceiptUpdater(repository, lineRepository);
        UpdateLineRequest presentLine = new UpdateLineRequest(UUID.randomUUID(), BigDecimal.TEN, "some-line-name");
        UpdateLineRequest toDeleteLine = new UpdateLineRequest(UUID.randomUUID(), BigDecimal.TEN, "some-line-name-delete");
        List<UpdateLineRequest> lines = List.of(presentLine,
            new UpdateLineRequest(UUID.randomUUID(), BigDecimal.TEN, "some-line-name-2"),
            new UpdateLineRequest(UUID.randomUUID(), BigDecimal.TEN, "some-line-name-3"));
        UpdateReceiptRequest receipt = givenUpdateRequest(lines);
        List<Line> realLines = lines.stream().map(UpdateLineRequest::toLine).toList();
        when(lineRepository.findInReceipt(receipt.id())).thenReturn(realLines);
        when(repository.update(any(Receipt.class))).thenReturn(true);

        updater.update(receipt);

        verify(lineRepository, atMostOnce()).update(realLines.get(0));
        verify(lineRepository, atMostOnce()).delete(List.of(toDeleteLine.id()));
        verify(lineRepository, atMostOnce()).save(realLines.get(1), receipt.id());
        verify(lineRepository, atMostOnce()).save(realLines.get(2), receipt.id());

    }

    private static UpdateReceiptRequest givenUpdateRequest() {
        return new UpdateReceiptRequest(UUID.randomUUID(), BigDecimal.TEN, "some-site", "some-text",
            List.of(new UpdateLineRequest(UUID.randomUUID(), BigDecimal.TEN, "some-line-name")), true, "FOOD");
    }

    private static UpdateReceiptRequest givenUpdateRequest(List<UpdateLineRequest> lines) {
        return new UpdateReceiptRequest(UUID.randomUUID(), BigDecimal.TEN, "some-site", "some-text", lines, true, "FOOD");
    }
}
