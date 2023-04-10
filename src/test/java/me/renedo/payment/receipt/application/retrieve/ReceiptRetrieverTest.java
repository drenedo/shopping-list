package me.renedo.payment.receipt.application.retrieve;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.atMostOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import me.renedo.payment.line.domain.Line;
import me.renedo.payment.line.domain.LineRepository;
import me.renedo.payment.receipt.domain.Receipt;
import me.renedo.payment.receipt.domain.ReceiptRepository;

class ReceiptRetrieverTest {

    @Test
    void retrieve_paginated() {
        ReceiptRepository repository = mock(ReceiptRepository.class);
        ReceiptRetriever retriever = new ReceiptRetriever(repository, null);

        retriever.retrievePaginated(LocalDateTime.now(), 5);

        verify(repository, atMostOnce()).findAllPaginate(any(LocalDateTime.class), eq(5));
    }

    @Test
    void retrieve_detail() {
        ReceiptRepository repository = mock(ReceiptRepository.class);
        LineRepository lineRepository = mock(LineRepository.class);
        ReceiptRetriever retriever = new ReceiptRetriever(repository, lineRepository);
        UUID uuid = UUID.randomUUID();
        List<Line> lines = List.of(new Line(UUID.randomUUID(), null, null, "some-name", BigDecimal.valueOf(12.3D), 1, LocalDateTime.now()));
        Receipt receipt = new Receipt(uuid, null, "some-text", BigDecimal.valueOf(12.3D), "some-site", lines, LocalDateTime.now(), true);
        when(repository.findById(uuid)).thenReturn(Optional.of(receipt));
        when(lineRepository.findInReceipt(uuid)).thenReturn(lines);

        Optional<Receipt> returned = retriever.retrieveDetail(uuid);

        assertTrue(returned.isPresent());
        assertThat(returned.get().getText(), is("some-text"));
        assertThat(returned.get().getLines(), hasSize(1));
    }


    @Test
    void retrieve_current_month() {
        ReceiptRepository repository = mock(ReceiptRepository.class);
        LineRepository lineRepository = mock(LineRepository.class);
        ReceiptRetriever retriever = new ReceiptRetriever(repository, lineRepository);
        when(repository.findAllBetweenDates(any(LocalDateTime.class), any(LocalDateTime.class))).
            thenReturn(
                List.of(new Receipt(UUID.randomUUID(), null, "some-text", BigDecimal.valueOf(12.3D), "some-site", null, LocalDateTime.now(), true)));

        double totalOfMonth = retriever.retrieveSumOfCurrentMonth(ZoneId.of("Europe/Madrid"));

        assertThat(totalOfMonth, is(12.3D));
    }
}
