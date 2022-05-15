package org.infoshare.rekinyfinansjeryweb.data;

import java.time.LocalDateTime;

public class OperationHistory {
    private LocalDateTime dateOpreation;
    private OperationEnum operation;
    private double exchangeRate;
    private double amount;

    public OperationHistory() {
    }

    public OperationHistory(LocalDateTime dateOpreation, OperationEnum operation, double exchangeRate, double amount) {
        this.dateOpreation = dateOpreation;
        this.operation = operation;
        this.exchangeRate = exchangeRate;
        this.amount = amount;
    }

    public LocalDateTime getDateOpreation() {
        return dateOpreation;
    }

    public void setDateOpreation(LocalDateTime dateOpreation) {
        this.dateOpreation = dateOpreation;
    }

    public OperationEnum getOperation() {
        return operation;
    }

    public void setOperation(OperationEnum operation) {
        this.operation = operation;
    }

    public double getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(double exchangeRate) {
        this.exchangeRate = exchangeRate;
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

        OperationHistory that = (OperationHistory) o;

        if (Double.compare(that.exchangeRate, exchangeRate) != 0) return false;
        if (Double.compare(that.amount, amount) != 0) return false;
        if (dateOpreation != null ? !dateOpreation.equals(that.dateOpreation) : that.dateOpreation != null)
            return false;
        return operation == that.operation;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = dateOpreation != null ? dateOpreation.hashCode() : 0;
        result = 31 * result + (operation != null ? operation.hashCode() : 0);
        temp = Double.doubleToLongBits(exchangeRate);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(amount);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "OperationHistory{" +
                "dateOpreation=" + dateOpreation +
                ", operation=" + operation +
                ", exchangeRate=" + exchangeRate +
                ", amount=" + amount +
                '}';
    }
}
