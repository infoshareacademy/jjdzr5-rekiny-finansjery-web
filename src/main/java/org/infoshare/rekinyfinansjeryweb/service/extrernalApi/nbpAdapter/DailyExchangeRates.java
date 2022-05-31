package org.infoshare.rekinyfinansjeryweb.service.extrernalApi.nbpAdapter;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class DailyExchangeRates {
    private String table;
    private String no;
    private LocalDate tradingDate;
    private LocalDate effectiveDate;
    private CopyOnWriteArrayList<ExchangeRate> rates;

    public DailyExchangeRates(){
        this.rates = new CopyOnWriteArrayList<ExchangeRate>();
    }

    public DailyExchangeRates(String table, String no, LocalDate tradingDate, LocalDate effectiveDate, CopyOnWriteArrayList<ExchangeRate> rates) {
        this.table = table;
        this.no = no.toUpperCase();
        this.tradingDate = tradingDate;
        this.effectiveDate = effectiveDate;
        this.rates = rates;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no.toUpperCase();
    }

    public LocalDate getTradingDate() {
        return tradingDate;
    }

    public void setTradingDate(LocalDate tradingDate) {
        this.tradingDate = tradingDate;
    }

    public LocalDate getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(LocalDate effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public CopyOnWriteArrayList<ExchangeRate> getRates() {
        return rates;
    }

    public void setRates(List<ExchangeRate> rates) {
        this.rates = new CopyOnWriteArrayList<>(rates);
    }

    public DailyExchangeRates copy(){
        return new DailyExchangeRates(table, no, tradingDate, effectiveDate, new CopyOnWriteArrayList<>(rates));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DailyExchangeRates that = (DailyExchangeRates) o;

        if (!table.equals(that.table)) return false;
        if (!no.equals(that.no)) return false;
        if (!tradingDate.equals(that.tradingDate)) return false;
        if (!effectiveDate.equals(that.effectiveDate)) return false;
        return rates.equals(that.rates);
    }

    @Override
    public int hashCode() {
        int result = table.hashCode();
        result = 31 * result + no.hashCode();
        result = 31 * result + tradingDate.hashCode();
        result = 31 * result + effectiveDate.hashCode();
        result = 31 * result + rates.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "ExchangeRatesTable{" +
                "table='" + table + '\'' +
                ", no='" + no + '\'' +
                ", tradingDate='" + tradingDate + '\'' +
                ", effectiveDate='" + effectiveDate + '\'' +
                ", rates=" + rates +
                '}';
    }
}
