package org.infoshare.rekinyfinansjeryweb.service.extrernalApi.nbpAdapter;

public class ExchangeRate {
    private String currency;
    private String code;
    private double bid;
    private double ask;

    public ExchangeRate(String currency, String code, double bid, double ask) {
        this.currency = currency;
        this.code = code.toUpperCase();
        this.bid = bid;
        this.ask = ask;
    }

    public ExchangeRate() {}

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code.toUpperCase();
    }

    public double getBid() {
        return bid;
    }

    public void setBid(double bid) {
        this.bid = bid;
    }

    public double getAsk() {
        return ask;
    }

    public void setAsk(double ask) {
        this.ask = ask;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ExchangeRate rate = (ExchangeRate) o;

        if (Double.compare(rate.bid, bid) != 0) return false;
        if (Double.compare(rate.ask, ask) != 0) return false;
        if (!currency.equals(rate.currency)) return false;
        return code.equals(rate.code);
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = currency.hashCode();
        result = 31 * result + code.hashCode();
        temp = Double.doubleToLongBits(bid);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(ask);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "ApiNbp.Rate{" +
                "currency='" + currency + '\'' +
                ", code='" + code + '\'' +
                ", bid=" + bid +
                ", ask=" + ask +
                '}';
    }
}
