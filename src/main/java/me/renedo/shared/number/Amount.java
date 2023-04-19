package me.renedo.shared.number;

public class Amount {

    private final Double amount;

    public Amount(Double amount){
        this.amount = amount;
    }

    public Amount(int intAMount){
        this.amount = (double) intAMount / 1000D;
    }

    public Double getAmount() {
        return amount;
    }

    public Integer getAmountWithoutDecimals(){
        return amount != null ? Double.valueOf(amount * 1000D).intValue() : null;
    }
}
