package me.renedo.payment.app.http.receipt.record;

import java.math.BigDecimal;
import java.util.UUID;

import me.renedo.payment.receipt.domain.Receipt;
import me.renedo.shared.date.ISOFormatter;

public record ReceiptResponse(UUID id, String text, String site, BigDecimal total, String created, Boolean cash, Integer lineNumber) {
    public ReceiptResponse(Receipt receipt) {
        this(receipt.getId(), receipt.getText(), receipt.getSite(), receipt.getTotal(), ISOFormatter.format(receipt.getCreated()), receipt.getCash(),
            receipt.getLineNumber());
    }
}
