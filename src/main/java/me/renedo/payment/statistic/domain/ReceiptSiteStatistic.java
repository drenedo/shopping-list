package me.renedo.payment.statistic.domain;

import java.math.BigDecimal;

import me.renedo.shared.string.TextIdentifier;

public class ReceiptSiteStatistic {

    private final String site;

    private final BigDecimal total;

    public ReceiptSiteStatistic(String site, BigDecimal total) {
        this.site = site;
        this.total = total;
    }

    public String getSite() {
        return site;
    }

    public String getIdentifier() {
        return TextIdentifier.getIdentifier(site);
    }

    public BigDecimal getTotal() {
        return total;
    }
}
