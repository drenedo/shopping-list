package me.renedo.payment.statistic.domain;

import me.renedo.payment.receipt.domain.Category;

public class ReceiptCategoryStatistic {
    private final Category category;

    private final double total;

    public ReceiptCategoryStatistic(Category category, double amount) {
        this.category = category;
        total = amount;
    }

    public double getTotal() {
        return total;
    }

    public Category getCategory() {
        return category;
    }
}
