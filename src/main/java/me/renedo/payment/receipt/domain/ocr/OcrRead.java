package me.renedo.payment.receipt.domain.ocr;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import me.renedo.payment.receipt.domain.Receipt;

public class OcrRead {
    private final String site;

    private final BigDecimal total;

    private final List<OcrLine> lines;

    private final LocalDate date;

    private final Boolean cash;

    private final String text;

    public OcrRead(String site, BigDecimal total, List<OcrLine> lines, LocalDate date, Boolean cash, String text) {
        this.site = site;
        this.total = total;
        this.lines = lines;
        this.date = date;
        this.cash = cash;
        this.text = text;
    }

    public String getSite() {
        return site;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public List<OcrLine> getLines() {
        return lines;
    }

    public LocalDate getDate() {
        return date;
    }

    public Boolean getCash() {
        return cash;
    }

    public String getText() {
        return text;
    }

    public Receipt toReceipt(UUID id, UUID list) {
        return new Receipt(id, list, text.toUpperCase(), total, site, lines.stream().map(OcrLine::toLine).collect(Collectors.toList()),
            LocalDateTime.now());
    }
}
