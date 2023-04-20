package me.renedo.payment.receipt.domain;

import java.util.Arrays;

public enum Category {
    FOOD('F'), HOUSE('H'), ENTERTAINMENT('E'), TRANSPORT('T'), CLOTHES('C');
    private final char id;

    Category(char id) {
        this.id = id;
    }

    public char getId() {
        return id;
    }

    public static Category valueOfId(String value){
        return value == null ? null : Arrays.stream(Category.values())
            .filter(c -> value.charAt(0) == c.getId() || c.toString().equals(value))
            .findFirst().orElse(null);
    }
}
