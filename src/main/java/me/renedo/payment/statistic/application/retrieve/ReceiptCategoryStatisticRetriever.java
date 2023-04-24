package me.renedo.payment.statistic.application.retrieve;

import java.time.LocalDateTime;
import java.util.List;

import me.renedo.payment.statistic.domain.ReceiptCategoryStatistic;
import me.renedo.payment.statistic.domain.ReceiptCategoryStatisticRepository;
import me.renedo.shared.Service;

@Service
public class ReceiptCategoryStatisticRetriever {

    private final ReceiptCategoryStatisticRepository repository;

    public ReceiptCategoryStatisticRetriever(ReceiptCategoryStatisticRepository repository) {
        this.repository = repository;
    }

    public List<ReceiptCategoryStatistic> calculateByPeriod(LocalDateTime from, LocalDateTime to){
        return repository.findByPeriod(from, to);
    }
}
