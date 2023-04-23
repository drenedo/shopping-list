package me.renedo.payment.statistic.domain;

import java.time.LocalDateTime;
import java.util.List;

public interface ReceiptSiteStatisticRepository {

    List<ReceiptSiteStatistic> findByPeriod(LocalDateTime from, LocalDateTime to);
}
