package org.infoshare.rekinyfinansjeryweb.dto.user;

import javax.validation.constraints.Min;

public class AmountFormDTO {

    @Min(value = 1, message ="{validation.amount.min}")
    private String amount;

    public AmountFormDTO() {
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AmountFormDTO that = (AmountFormDTO) o;

        return amount != null ? amount.equals(that.amount) : that.amount == null;
    }

    @Override
    public int hashCode() {
        return amount != null ? amount.hashCode() : 0;
    }
}
