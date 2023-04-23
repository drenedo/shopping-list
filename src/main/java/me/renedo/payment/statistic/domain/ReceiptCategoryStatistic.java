package me.renedo.payment.statistic.domain;

import me.renedo.payment.receipt.domain.Category;

public class ReceiptCategoryStatistic {
    private final Category category;

    private final double amount;

    public ReceiptCategoryStatistic(Category category, double amount) {
        this.category = category;
        this.amount = amount;
    }

    public double getAmount() {
        return amount;
    }

    public Category getCategory() {
        return category;
    }
}
