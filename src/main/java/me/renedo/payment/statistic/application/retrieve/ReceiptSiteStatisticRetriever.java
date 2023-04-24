package me.renedo.payment.statistic.application.retrieve;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import me.renedo.payment.statistic.domain.ReceiptSiteStatistic;
import me.renedo.payment.statistic.domain.ReceiptSiteStatisticRepository;
import me.renedo.shared.Service;

@Service
public class ReceiptSiteStatisticRetriever {

    private final ReceiptSiteStatisticRepository repository;

    public ReceiptSiteStatisticRetriever(ReceiptSiteStatisticRepository repository) {
        this.repository = repository;
    }

    public List<ReceiptSiteStatistic> calculateByPeriod(LocalDateTime from, LocalDateTime to) {
        List<ReceiptSiteStatistic> statistics = repository.findByPeriod(from, to);
        return statistics.stream().collect(Collectors.groupingBy(ReceiptSiteStatistic::getIdentifier)).values().stream()
            .map(l -> new ReceiptSiteStatistic(l.stream().findFirst().get().getSite(),
                l.stream().map(ReceiptSiteStatistic::getTotal).reduce(BigDecimal.ZERO, BigDecimal::add)))
            .toList();
    }
}
