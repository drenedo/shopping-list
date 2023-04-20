package me.renedo.payment.receipt.application.create;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import me.renedo.payment.line.domain.LineRepository;
import me.renedo.payment.receipt.domain.Receipt;
import me.renedo.payment.receipt.domain.ReceiptRepository;
import me.renedo.payment.receipt.domain.ocr.OcrRead;
import me.renedo.payment.receipt.domain.ocr.OcrService;

class ReceiptCreatorTest {

    @Test
    void save_simple_item() {
        ReceiptRepository repository = mock(ReceiptRepository.class);
        OcrService ocrService = mock(OcrService.class);
        LineRepository lineRepository = mock(LineRepository.class);
        ReceiptCreator creator = new ReceiptCreator(ocrService, repository, lineRepository);
        UUID id = UUID.randomUUID();

        creator.create(id, "some-site", 45.2D, null, null);

        verify(repository, atLeastOnce()).save(any(Receipt.class));
    }

    @Test
    void save_a_valid_item() {
        ReceiptRepository repository = mock(ReceiptRepository.class);
        OcrService ocrService = mock(OcrService.class);
        LineRepository lineRepository = mock(LineRepository.class);
        LocalDate now = LocalDate.now();
        when(ocrService.read("/file/kk")).thenReturn(new OcrRead("some-site", new BigDecimal(10), List.of(), now, true, "some-text"));
        ReceiptCreator creator = new ReceiptCreator(ocrService, repository, lineRepository);
        UUID id = UUID.randomUUID();
        UUID listId = UUID.randomUUID();

        creator.create(new ReceiptCreator.CreateReceiptRequest("/file/kk", id, listId));

        verify(repository, atLeastOnce()).save(any(Receipt.class));
    }
}
