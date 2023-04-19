package me.renedo.payment.app.http.receipt.record;

import java.math.BigDecimal;
import java.util.UUID;

import me.renedo.payment.line.domain.Line;
import me.renedo.shared.date.ISOFormatter;

public record LineResponse(UUID id, UUID item, String name, BigDecimal total, Double amount, String updated){
    public LineResponse(Line line){
        this(line.getId(), line.getItem(), line.getName(), line.getTotal(), line.getAmount(),  ISOFormatter.format(line.getCreated()));
    }
}
