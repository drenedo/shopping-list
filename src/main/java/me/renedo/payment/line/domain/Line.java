package me.renedo.payment.line.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import me.renedo.shared.exception.NotAcceptableException;

public class Line {
    private final UUID id;

    private final UUID receipt;

    private final UUID item;

    private final String name;

    private final BigDecimal total;

    private final Integer amount;

    private final LocalDateTime created;

    public Line(UUID id, UUID receipt, UUID item, String name, BigDecimal total, Integer amount, LocalDateTime updated) {
        if(id == null){
            throw new NotAcceptableException("Id is mandatory");
        }
        if(name == null || name.isEmpty()){
            throw new NotAcceptableException("Name is mandatory");
        }
        if(total == null){
            throw new NotAcceptableException("Total amount is mandatory");
        }
        this.id = id;
        this.item = item;
        this.receipt = receipt;
        this.name = name;
        this.total = total;
        this.amount = amount;
        this.created = updated == null ? LocalDateTime.now() : updated;
    }

    public UUID getId() {
        return id;
    }

    public UUID getReceipt() {
        return receipt;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public Integer getAmount() {
        return amount;
    }

    public UUID getItem() {
        return item;
    }

    public LocalDateTime getCreated() {
        return created;
    }
}
