package me.renedo.payment.statistic.infraestructure;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import me.renedo.InfrastructureTestCase;
import me.renedo.payment.statistic.domain.ReceiptCategoryStatistic;

class JooqReceiptCategoryStatisticRepositoryTest extends InfrastructureTestCase {
    private final JooqReceiptCategoryStatisticRepository jooqReceiptCategoryStatisticRepository;

    @Autowired
    JooqReceiptCategoryStatisticRepositoryTest(JooqReceiptCategoryStatisticRepository jooqReceiptCategoryStatisticRepository) {
        this.jooqReceiptCategoryStatisticRepository = jooqReceiptCategoryStatisticRepository;
    }

    @Test
    public void test_find_statistic_for_category_of_receipts() {
        List<ReceiptCategoryStatistic> statistics =
            jooqReceiptCategoryStatisticRepository.findByPeriod(LocalDateTime.of(2023, 2, 1, 0, 0), LocalDateTime.of(2023, 2, 20, 0, 0));

        assertThat(statistics, hasSize(4));
    }
}
