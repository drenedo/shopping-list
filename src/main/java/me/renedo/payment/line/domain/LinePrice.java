package me.renedo.payment.line.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.UUID;

public class LinePrice {
    private final UUID id;

    private final String name;

    private final String site;

    private final BigDecimal total;

    private final LocalDateTime created;

    private final Double amount;

    public LinePrice(UUID id, String name, String site, BigDecimal total, LocalDateTime created, Double amount) {
        this.id = id;
        this.name = name;
        this.site = site;
        this.total = total;
        this.created = created;
        this.amount = amount;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSite() {
        return site;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public Double getAmount() {
        return amount;
    }

    public Double getPrice() {
        return amount != null ? total.divide(BigDecimal.valueOf(amount), 3, RoundingMode.HALF_UP).doubleValue() : null;
    }
}
