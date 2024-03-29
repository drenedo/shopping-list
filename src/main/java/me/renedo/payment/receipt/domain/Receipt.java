package me.renedo.payment.receipt.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import me.renedo.payment.line.domain.Line;
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

    private final Category category;

    public Receipt(Receipt receipt, List<Line> lines) {
        this(receipt.getId(), receipt.getList(), receipt.getText(), receipt.getTotal(), receipt.getSite(), lines, receipt.getCreated(),
            receipt.getCash(), lines.size(), receipt.getCategory());
    }

    public Receipt(UUID id, UUID list, String text, BigDecimal total, String site, List<Line> lines, LocalDateTime created, Boolean cash,
        Category category) {
        this(id, list, text, total, site, lines, created, cash, null, category);
    }

    public Receipt(UUID id, UUID list, String text, BigDecimal total, String site, List<Line> lines, LocalDateTime created, Boolean cash,
        Integer lineNumber, Category category) {
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
        this.category = category;
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

    public Integer getLineNumber() {
        return lineNumber;
    }

    public Boolean getCash() {
        return cash;
    }

    public Category getCategory() {
        return category;
    }

    public Receipt getReceiptWithCorrectLinesIfIsPossible() {
        if (lines == null || lines.isEmpty() || total.doubleValue() == getTotalOfLines(lines)) {
            return this;
        }
        return new Receipt(id, list, text, total, site, findCorrectSum(), created, cash, lineNumber, category);
    }

    public List<Line> findCorrectSum() {
        List<Line> finalLines = findCorrectSum(lines);
        if (finalLines == null) {
            throw new NotAcceptableException("Lines not match with total");
        }
        return finalLines;
    }

    private List<Line> findCorrectSum(List<Line> lines) {
        double totalOfLines = getTotalOfLines(lines);
        double total = this.getTotal().doubleValue();

        for (int i = 0; i < lines.size(); i++) {
            if (totalOfLines - lines.get(i).getTotal().doubleValue() == total) {
                return getPartialList(lines, i);
            }
        }
        for (int i = 0; i < lines.size(); i++) {
            List<Line> partial = getPartialList(lines, i);
            double sumPartial = getTotalOfLines(partial);
            if (total == sumPartial) {
                return partial;
            }
            if (total <= getTotalOfLines(partial)) {
                List<Line> newPartial = findCorrectSum(partial);
                if (newPartial != null) {
                    return newPartial;
                }
            }
        }
        return null;
    }

    private List<Line> getPartialList(List<Line> lines, int index) {
        List<Line> newLines = new ArrayList<>(lines);
        newLines.remove(index);
        return newLines;
    }

    private double getTotalOfLines(List<Line> lines) {
        return lines.stream().map(Line::getTotal).reduce(BigDecimal.ZERO, BigDecimal::add).doubleValue();
    }
}
