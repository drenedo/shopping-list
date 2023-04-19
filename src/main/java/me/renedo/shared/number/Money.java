package me.renedo.shared.number;

import java.math.BigDecimal;

public class Money {

    private final static BigDecimal DECIMALS = new BigDecimal(100);
    private final BigDecimal amount;

    public Money(BigDecimal amount) {
        this.amount = amount;
    }

    public Money(int money){
        amount = new BigDecimal((double) money / 100D);
    }

    public BigDecimal getMoney(){
        return amount;
    }

    public int getMoneyWithoutDecimals(){
        return amount.multiply(DECIMALS).toBigInteger().intValue();
    }
}
