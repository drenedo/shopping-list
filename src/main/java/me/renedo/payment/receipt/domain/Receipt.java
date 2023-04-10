package me.renedo.payment.receipt.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import me.renedo.payment.line.domain.Line;
import me.renedo.shared.date.ISOFormatter;
import me.renedo.shared.exception.NotAcceptableException;

public class Receipt {
    private final UUID id;

    private final UUID list;

    private final String text;

    private final BigDecimal total;

    private final String site;

    private final List<Line> lines;

    private final Boolean cash;

    private final LocalDateTime created;

    private final Integer lineNumber;

    public Receipt(Receipt receipt, List<Line> lines) {
        this(receipt.getId(), receipt.getList(), receipt.getText(), receipt.getTotal(), receipt.getSite(), lines, receipt.getCreated(),
            receipt.getCash(), lines.size());
    }

    public Receipt(UUID id, UUID list, String text, BigDecimal total, String site, List<Line> lines, LocalDateTime created, Boolean cash) {
        this(id, list, text, total, site, lines, created, cash, null);
    }

    public Receipt(UUID id, UUID list, String text, BigDecimal total, String site, List<Line> lines, LocalDateTime created, Boolean cash,
        Integer lineNumber) {
        if (id == null) {
            throw new NotAcceptableException("Id is mandatory");
        }
        if (text == null || text.isEmpty()) {
            throw new NotAcceptableException("Text is mandatory");
        }
        if (total == null) {
            throw new NotAcceptableException("Total amount is mandatory");
        }
        if (site == null) {
            throw new NotAcceptableException("Site is mandatory");
        }
        this.id = id;
        this.list = list;
        this.text = text;
        this.total = total;
        this.site = site;
        this.lines = lines;
        this.created = created == null ? LocalDateTime.now() : created;
        this.cash = cash;
        this.lineNumber = lineNumber != null ? lineNumber : (lines == null ? null : lines.size());
    }

    public UUID getId() {
        return id;
    }

    public UUID getList() {
        return list;
    }

    public String getText() {
        return text;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public String getSite() {
        return site;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public List<Line> getLines() {
        return lines;
    }

    public String getStringCreated() {
        return ISOFormatter.format(created);
    }

    public Integer getLineNumber() {
        return lineNumber;
    }

    public Boolean getCash() {
        return cash;
    }
}
