package me.renedo.payment.line.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class LinePrice {
    private final UUID id;

    private final String name;

    private final String site;

    private final BigDecimal total;

    private final LocalDateTime created;

    public LinePrice(UUID id, String name, String site, BigDecimal total, LocalDateTime created) {
        this.id = id;
        this.name = name;
        this.site = site;
        this.total = total;
        this.created = created;
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
}
