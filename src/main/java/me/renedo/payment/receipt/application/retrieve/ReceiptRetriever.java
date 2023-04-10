package me.renedo.payment.receipt.application.retrieve;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import me.renedo.payment.line.domain.LineRepository;
import me.renedo.payment.receipt.domain.Receipt;
import me.renedo.payment.receipt.domain.ReceiptRepository;
import me.renedo.shared.Service;

@Service
public class ReceiptRetriever {

    private final ReceiptRepository repository;

    private final LineRepository lineRepository;

    public ReceiptRetriever(ReceiptRepository repository, LineRepository lineRepository) {
        this.repository = repository;
        this.lineRepository = lineRepository;
    }

    public List<Receipt> retrievePaginated(LocalDateTime localDateTime, int pageSize){
        return repository.findAllPaginate(localDateTime, pageSize);
    }

    public Optional<Receipt> retrieveDetail(UUID id) {
        Optional<Receipt> receiptWithoutLines = repository.findById(id);
        return receiptWithoutLines.map(this::retrieveReceiptWithLines);
    }

    public double retrieveSumOfCurrentMonth(ZoneId timeZone){
        LocalDate now = LocalDate.now();
        LocalDateTime start = now.withDayOfMonth(1).atStartOfDay(timeZone).toLocalDateTime();
        LocalDateTime end = now.withDayOfMonth(now.getMonth().length(now.isLeapYear())).plusDays(1).atStartOfDay(timeZone).toLocalDateTime();
        return repository.findAllBetweenDates(start, end)
            .stream().mapToDouble(r -> r.getTotal().doubleValue())
            .sum();
    }

    private Receipt retrieveReceiptWithLines(Receipt receipt){
        return new Receipt(receipt, lineRepository.findInReceipt(receipt.getId()));
    }
}
