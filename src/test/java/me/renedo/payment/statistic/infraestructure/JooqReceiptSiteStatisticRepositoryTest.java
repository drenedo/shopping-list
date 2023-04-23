package me.renedo.payment.statistic.infraestructure;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.Is.is;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import me.renedo.InfrastructureTestCase;
import me.renedo.payment.statistic.domain.ReceiptSiteStatistic;

class JooqReceiptSiteStatisticRepositoryTest extends InfrastructureTestCase {
    private final JooqReceiptSiteStatisticRepository jooqReceiptSiteStatisticRepository;

    @Autowired
    JooqReceiptSiteStatisticRepositoryTest(JooqReceiptSiteStatisticRepository jooqReceiptSiteStatisticRepository) {
        this.jooqReceiptSiteStatisticRepository = jooqReceiptSiteStatisticRepository;
    }

    @Test
    public void test_find_statistic_for_category_of_receipts() {
        List<ReceiptSiteStatistic> statistics =
            jooqReceiptSiteStatisticRepository.findByPeriod(LocalDateTime.of(2023, 2, 1, 0, 0), LocalDateTime.of(2023, 2, 20, 0, 0));

        assertThat(statistics, hasSize(3));
        assertThat(statistics.get(0).getSite(), is("ALCAMPO 1"));
        assertThat(statistics.get(0).getTotal().doubleValue(), is(30.5D));
        assertThat(statistics.get(1).getSite(), is("ALCAMPO 3"));
        assertThat(statistics.get(1).getTotal().doubleValue(), is(61D));
        assertThat(statistics.get(2).getSite(), is("ALCAMPO 4"));
        assertThat(statistics.get(2).getTotal().doubleValue(), is(30.5D));
    }
}
