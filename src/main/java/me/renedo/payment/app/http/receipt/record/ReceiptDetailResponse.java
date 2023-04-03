package me.renedo.payment.app.http.receipt.record;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import me.renedo.payment.receipt.domain.Receipt;

public record ReceiptDetailResponse(UUID id, String text, String site, BigDecimal total, String created, List<LineResponse> lines) {
    public ReceiptDetailResponse(Receipt receipt) {
        this(receipt.getId(), receipt.getText(), receipt.getSite(), receipt.getTotal(), receipt.getStringCreated(),
            receipt.getLines() != null ? receipt.getLines().stream().map(LineResponse::new).toList() : null);
    }
}
