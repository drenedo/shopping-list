package me.renedo.payment.app.http.receipt;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import me.renedo.app.http.V1Controller;
import me.renedo.payment.app.http.receipt.record.ReceiptDetailResponse;
import me.renedo.payment.app.http.receipt.record.ReceiptResponse;
import me.renedo.payment.receipt.application.retrieve.ReceiptRetriever;
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
}
