package org.infoshare.rekinyfinansjeryweb.data;

public class CurrencyOperationHistory extends OperationHistory{
    private String currency;

    public CurrencyOperationHistory(OperationHistory operationHistory, String currency) {
        super();
        setOperation(operationHistory.getOperation());
        setAmount(operationHistory.getAmount());
        setDateOperation(operationHistory.getDateOperation());
        setExchangeRate(operationHistory.getExchangeRate());
        this.currency = currency;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;

    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        CurrencyOperationHistory that = (CurrencyOperationHistory) o;

        return currency != null ? currency.equals(that.currency) : that.currency == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (currency != null ? currency.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "CurrencyOperationHistory{" +
                "currency='" + currency + '\'' +
                '}';
    }
}
