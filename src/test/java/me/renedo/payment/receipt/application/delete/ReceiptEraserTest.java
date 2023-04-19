package me.renedo.payment.receipt.application.delete;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import me.renedo.payment.line.domain.Line;
import me.renedo.payment.line.domain.LineRepository;
import me.renedo.payment.receipt.domain.ReceiptRepository;
import me.renedo.shared.exception.NotFoundException;

class ReceiptEraserTest {

    @Test
    void delete_receipt_doesnt_exist() {
        ReceiptRepository repository = mock(ReceiptRepository.class);
        LineRepository lineRepository = mock(LineRepository.class);
        ReceiptEraser eraser = new ReceiptEraser(repository, lineRepository);
        UUID id = UUID.randomUUID();
        when(repository.delete(id)).thenReturn(0);

        assertThrows(NotFoundException.class, () -> eraser.delete(id));
    }

    @Test
    void delete_receipt_exists() {
        ReceiptRepository repository = mock(ReceiptRepository.class);
        LineRepository lineRepository = mock(LineRepository.class);
        ReceiptEraser eraser = new ReceiptEraser(repository, lineRepository);
        UUID id = UUID.randomUUID();
        when(repository.delete(id)).thenReturn(1);
        when(lineRepository.findInReceipt(id)).thenReturn(
            List.of(givenLine("d44f860a-0d91-0529-9f91-ac9f5f29a35c"), givenLine("d44f860a-0d91-0529-9f91-ac9f5f29a35f")));
        when(lineRepository.delete(anyList())).thenReturn(2);

        eraser.delete(id);

        verify(lineRepository, atLeastOnce()).delete(
            List.of(UUID.fromString("d44f860a-0d91-0529-9f91-ac9f5f29a35c"), UUID.fromString("d44f860a-0d91-0529-9f91-ac9f5f29a35f")));
    }

    @Test
    void delete_receipt_when_error_deleting_lists() {
        ReceiptRepository repository = mock(ReceiptRepository.class);
        LineRepository lineRepository = mock(LineRepository.class);
        ReceiptEraser eraser = new ReceiptEraser(repository, lineRepository);
        UUID id = UUID.randomUUID();
        when(repository.delete(id)).thenReturn(1);
        when(lineRepository.findInReceipt(id)).thenReturn(
            List.of(givenLine("d44f860a-0d91-0529-9f91-ac9f5f29a35c"), givenLine("d44f860a-0d91-0529-9f91-ac9f5f29a35f")));

        assertThrows(RuntimeException.class, () -> eraser.delete(id));
    }

    private Line givenLine(String id) {
        return new Line(UUID.fromString(id), null, null, "some-name", BigDecimal.ONE, 1D, null);
    }


}
