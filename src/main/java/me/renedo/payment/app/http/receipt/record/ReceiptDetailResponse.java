package me.renedo.payment.app.http.receipt.record;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import me.renedo.payment.receipt.domain.Receipt;
import me.renedo.shared.date.ISOFormatter;

public record ReceiptDetailResponse(UUID id, String text, String site, BigDecimal total, String created, List<LineResponse> lines, Boolean cash,
                                    Integer lineNumber, String category) {
    public ReceiptDetailResponse(Receipt receipt) {
        this(receipt.getId(), receipt.getText(), receipt.getSite(), receipt.getTotal(), ISOFormatter.format(receipt.getCreated()),
            receipt.getLines() != null ? receipt.getLines().stream().map(LineResponse::new).toList() : null, receipt.getCash(),
            receipt.getLineNumber(), receipt.getCategory() != null ? receipt.getCategory().toString() : null);
    }
}
