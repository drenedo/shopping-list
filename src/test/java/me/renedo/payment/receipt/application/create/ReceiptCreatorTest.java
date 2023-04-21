package me.renedo.payment.receipt.application.create;

import static me.renedo.payment.receipt.domain.Category.HOUSE;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;

import me.renedo.payment.line.domain.LineRepository;
import me.renedo.payment.receipt.application.retrieve.ClassifierRetriever;
import me.renedo.payment.receipt.domain.Classifier;
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
        ClassifierRetriever classifierRetriever = mock(ClassifierRetriever.class);
        ClassifierCreator classifierCreator = mock(ClassifierCreator.class);
        when(classifierRetriever.findClassifier("some-site")).thenReturn(Optional.of(new Classifier(null, "some-site", HOUSE)));
        ReceiptCreator creator = new ReceiptCreator(ocrService, repository, classifierRetriever, classifierCreator, lineRepository);
        UUID id = UUID.randomUUID();

        Receipt receipt = creator.create(id, "some-site", 45.2D, null, null);

        verify(repository, atLeastOnce()).save(any(Receipt.class));
        MatcherAssert.assertThat(receipt.getCategory(), is(HOUSE));
    }

    @Test
    void save_a_valid_item() {
        ReceiptRepository repository = mock(ReceiptRepository.class);
        OcrService ocrService = mock(OcrService.class);
        LineRepository lineRepository = mock(LineRepository.class);
        ClassifierRetriever classifierRetriever = mock(ClassifierRetriever.class);
        ClassifierCreator classifierCreator = mock(ClassifierCreator.class);
        ReceiptCreator creator = new ReceiptCreator(ocrService, repository, classifierRetriever, classifierCreator, lineRepository);
        LocalDate now = LocalDate.now();
        when(classifierRetriever.findClassifier("some-site")).thenReturn(Optional.of(new Classifier(null, "some-site", HOUSE)));
        when(ocrService.read("/file/kk")).thenReturn(new OcrRead("some-site", new BigDecimal(10), List.of(), now, true, "some-text"));
        UUID id = UUID.randomUUID();
        UUID listId = UUID.randomUUID();

        Receipt receipt = creator.create(new ReceiptCreator.CreateReceiptRequest("/file/kk", id, listId));

        verify(repository, atLeastOnce()).save(any(Receipt.class));
        MatcherAssert.assertThat(receipt.getCategory(), is(HOUSE));

    }
}
