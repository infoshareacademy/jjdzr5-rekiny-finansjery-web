package org.infoshare.rekinyfinansjeryweb.dto;

import javax.validation.constraints.Min;

public class AmountFormDTO {

    @Min(value = 1, message ="{validation.amount.min}")
    private double amount;

    public AmountFormDTO() {
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AmountFormDTO that = (AmountFormDTO) o;

        return Double.compare(that.amount, amount) == 0;
    }

    @Override
    public int hashCode() {
        long temp = Double.doubleToLongBits(amount);
        return (int) (temp ^ (temp >>> 32));
    }

}
