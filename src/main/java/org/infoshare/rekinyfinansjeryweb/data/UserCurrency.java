package org.infoshare.rekinyfinansjeryweb.data;

import java.util.List;

public class UserCurrency {
    private double amount;
    private List<OperationHistory> historyList;

    public UserCurrency() {
    }

    public UserCurrency(double amount, List<OperationHistory> historyList) {
        this.amount = amount;
        this.historyList = historyList;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public List<OperationHistory> getHistoryList() {
        return historyList;
    }

    public void setHistoryList(List<OperationHistory> historyList) {
        this.historyList = historyList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserCurrency currency = (UserCurrency) o;

        if (Double.compare(currency.amount, amount) != 0) return false;
        return historyList != null ? historyList.equals(currency.historyList) : currency.historyList == null;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(amount);
        result = (int) (temp ^ (temp >>> 32));
        result = 31 * result + (historyList != null ? historyList.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Currency{" +
                "amount=" + amount +
                ", historyList=" + historyList +
                '}';
    }
}
