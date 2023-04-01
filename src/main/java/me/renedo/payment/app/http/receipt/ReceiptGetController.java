package me.renedo.payment.app.http.receipt;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import me.renedo.app.http.V1Controller;
import me.renedo.payment.line.domain.Line;
import me.renedo.payment.receipt.application.retrieve.ReceiptRetriever;
import me.renedo.payment.receipt.domain.Receipt;
import me.renedo.shared.exception.NotFoundException;
import me.renedo.shared.uuid.UUIDValidator;

@RestController
public class ReceiptGetController extends V1Controller {

    private final ReceiptRetriever retriever;

    public ReceiptGetController(ReceiptRetriever retriever) {
        this.retriever = retriever;
    }

    @GetMapping("/receipts/size/{size}")
    public List<ReceiptResponse> paginate(@PathVariable Integer size,
        @RequestParam(value = "date-time", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateTime) {
        return retriever.retrievePaginated(dateTime, size).stream()
            .map(ReceiptResponse::new).toList();
    }

    @GetMapping("/receipts/detail/{id}")
    public ReceiptDetailResponse detail(@PathVariable String id) {
        return retriever.retrieveDetail(UUIDValidator.fromString(id))
            .map(ReceiptDetailResponse::new)
            .orElseThrow(() -> new NotFoundException("Receipt not found"));
    }

    record ReceiptResponse(UUID id, String text, String site, BigDecimal total, String created) {
        ReceiptResponse(Receipt receipt) {
            this(receipt.getId(), receipt.getText(), receipt.getSite(), receipt.getTotal(), receipt.getStringCreated());
        }
    }

    record ReceiptDetailResponse(UUID id, String text, String site, BigDecimal total, String created, List<LineResponse> lines) {
        ReceiptDetailResponse(Receipt receipt) {
            this(receipt.getId(), receipt.getText(), receipt.getSite(), receipt.getTotal(), receipt.getStringCreated(),
                receipt.getLines().stream().map(LineResponse::new).toList());
        }
    }

    record LineResponse(UUID id, UUID item, String name, BigDecimal total, Integer amount, String updated){
        LineResponse(Line line){
            this(line.getId(), line.getItem(), line.getName(), line.getTotal(), line.getAmount(), line.getStringCreated());
        }
    }
}