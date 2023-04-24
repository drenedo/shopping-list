package me.renedo.payment.app.http.statistic;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import me.renedo.app.http.V1Controller;
import me.renedo.payment.statistic.application.retrieve.ReceiptCategoryStatisticRetriever;
import me.renedo.payment.statistic.application.retrieve.ReceiptSiteStatisticRetriever;
import me.renedo.payment.statistic.domain.ReceiptCategoryStatistic;
import me.renedo.payment.statistic.domain.ReceiptSiteStatistic;

@RestController
public class StatisticGetController extends V1Controller {

    private final ReceiptSiteStatisticRetriever siteRetriever;

    private final ReceiptCategoryStatisticRetriever categoryRetriever;


    public StatisticGetController(ReceiptSiteStatisticRetriever siteRetriever, ReceiptCategoryStatisticRetriever categoryRetriever) {
        this.siteRetriever = siteRetriever;
        this.categoryRetriever = categoryRetriever;
    }

    @GetMapping("/statistic/site/{from}/{to}")
    public List<ReceiptSiteStatisticResponse> siteStatistics(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
        @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to) {
        return siteRetriever.calculateByPeriod(from, to).stream()
            .map(ReceiptSiteStatisticResponse::new)
            .toList();
    }

    @GetMapping("/statistic/category/{from}/{to}")
    public List<ReceiptCategoryStatisticResponse> categoryStatistics(
        @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
        @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to) {
        return categoryRetriever.calculateByPeriod(from, to).stream()
            .map(ReceiptCategoryStatisticResponse::new)
            .toList();
    }

    public record ReceiptSiteStatisticResponse(String site, BigDecimal total) {
        ReceiptSiteStatisticResponse(ReceiptSiteStatistic statistic) {
            this(statistic.getSite(), statistic.getTotal());
        }
    }

    public record ReceiptCategoryStatisticResponse(String category, char id, double total) {
        ReceiptCategoryStatisticResponse(ReceiptCategoryStatistic statistic) {
            this(statistic.getCategory() != null ? statistic.getCategory().toString() : "OTHER",
                statistic.getCategory() != null ? statistic.getCategory().getId() : '?', statistic.getTotal());
        }
    }
}
