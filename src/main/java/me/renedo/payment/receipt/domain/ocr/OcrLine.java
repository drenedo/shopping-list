package me.renedo.payment.receipt.domain.ocr;

import java.math.BigDecimal;
import java.util.UUID;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import me.renedo.payment.line.domain.Line;

public class OcrLine {
    private final String name;
    private final Double amount;
    private final BigDecimal price;
    private final BigDecimal total;

    private final String text;

    public OcrLine(String name, Double amount, BigDecimal price, BigDecimal total, String text) {
        this.name = name;
        this.amount = amount;
        this.price = price;
        this.total = total;
        this.text = text;
    }

    public String getName() {
        return name;
    }

    public Double getAmount() {
        return amount;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public BigDecimal getTotal() {
        return total;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;

        if (o == null || getClass() != o.getClass())
            return false;

        OcrLine ocrLine = (OcrLine) o;

        return new EqualsBuilder().append(name, ocrLine.name).append(total, ocrLine.total).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(name).append(total).toHashCode();
    }

    public String getText() {
        return text;
    }

    public Line toLine() {
        return new Line(UUID.randomUUID(), null, null, name, total, amount, null);
    }
}
