package me.renedo.payment.statistic.domain;

import java.time.LocalDateTime;
import java.util.List;

public interface ReceiptCategoryStatisticRepository {

    List<ReceiptCategoryStatistic> findByPeriod(LocalDateTime from, LocalDateTime to);
}
